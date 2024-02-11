package client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;

import client.exception.unchecked.InvalidStartingParameterException;

import messagesbase.ResponseEnvelope;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;

import reactor.core.publisher.Mono;

/**
 * This class creates the queries and executes them using WebClient defined in Spring and is called from the ClientNetwork.
 */
public class NetworkQueryBuilder {

	private final String gameId;
	private final WebClient baseWebClient;
	private final static int EXPECTED_GAME_ID_LENGTH = 5;
	private final static String EXPECTED_ENDPOINT_URL = "http://swe1.wst.univie.ac.at:18235";
	private final static Logger logger = LoggerFactory.getLogger(NetworkQueryBuilder.class);
	
	/**
	 * The ctor also validates the input data. If invalid an InvalidStartingParameterException will be thrown.
	 * @param serverBaseUrl The endpoint of the server.
	 * @param gameId The corresponding game id.
	 */
	public NetworkQueryBuilder(final String serverBaseUrl, final String gameId) {
		// Test if the URL passed is valid.
		if(!serverBaseUrl.equals(EXPECTED_ENDPOINT_URL)) {
			logger.warn("Invalid endpoint URL: {}", serverBaseUrl);
			throw new InvalidStartingParameterException("Invalid URL starting parameter.");
		}
						
		// Test if gameId length is exactly 5.
		if(gameId.length() != EXPECTED_GAME_ID_LENGTH) {
			logger.warn("Invalid Game ID: {}", gameId);
			throw new InvalidStartingParameterException("Invalid game ID length.");
		}
		
		// Check to see if the gameId contains only digits and letters.
		for(int index = 0; index < gameId.length(); ++index) {
			final Character ch = gameId.charAt(index);
			if(!Character.isLetterOrDigit(ch)) {
				logger.warn("Invalid Game ID: {}, invalid character: {}", gameId, ch);
				throw new InvalidStartingParameterException("Game ID should contain only digits and letters.");
			}
		}
			
						
		this.gameId = gameId;
		this.baseWebClient = WebClient
								.builder()
								.baseUrl(serverBaseUrl + "/games")
								.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) 
								.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
								.build();
	}
	
	/**
	 * @param netOp The specific network operation - register, send halfmap, send move or request game state.
	 * @param toBeSent The object that has to be sent depending on the network operation.
	 * @return Returns a Mono object holding the response of the server encapsulated in a ResponseEnvelope object.
	 */
	public Mono<ResponseEnvelope> createQuery(final ENetworkOperation netOp, final Object toBeSent) {
		HttpMethod httpMethod = null;
		String uri = "/" + this.gameId;
		
		// Build the specific URI based on the operation you want to carry out.
		switch(netOp) {
			case REGISTER:
				httpMethod = HttpMethod.POST;
				uri += "/players";
				break;
			case SEND_HALFMAP:
				httpMethod = HttpMethod.POST;
				uri += "/halfmaps";
				break;
			case SEND_MOVE:
				httpMethod = HttpMethod.POST;
				uri += "/moves";
				break;
			case REQUEST_GAMESTATE:
				httpMethod = HttpMethod.GET;
				uri += "/states/" + (String)toBeSent;
				break;
		}
		
		logger.debug("{}, [{}]", netOp, EXPECTED_ENDPOINT_URL + uri);
		// First half of query to be sent to the server. Specifying the Http method + URI.
		final RequestBodySpec query = this.baseWebClient
												.method(httpMethod)
												.uri(uri);
		
		// Add additional information to the query. Relevant for the POST queries where additional info has to be passed.
		switch(netOp) {
			case REGISTER:
				return query
						.body(BodyInserters.fromValue((PlayerRegistration) toBeSent))
						.retrieve()
						.bodyToMono(ResponseEnvelope.class);
			case SEND_HALFMAP:
				return query
						.body(BodyInserters.fromValue((PlayerHalfMap) toBeSent))
						.retrieve()
						.bodyToMono(ResponseEnvelope.class);
			case SEND_MOVE:
				return query
							.body(BodyInserters.fromValue((PlayerMove) toBeSent)) 
							.retrieve()
							.bodyToMono(ResponseEnvelope.class);
			case REQUEST_GAMESTATE:
				return query
							.retrieve()
							.bodyToMono(ResponseEnvelope.class); 
			default:
				return null;
		}
	}

}