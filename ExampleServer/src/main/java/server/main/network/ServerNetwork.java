package server.main.network;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;

import server.main.controller.ServerController;
import server.exceptions.GenericExampleException;

@RestController
@RequestMapping(value = "/games")
public class ServerNetwork {

	@Autowired
	private ServerController controller;
	
	/*
	 * Returns random game id wrapped in UniqueGameIdentifier.
	 */
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {
		
		final String newGameId = this.controller.createNewGame();	
		
		return UniqueGameIdentifier.of(newGameId);
	}

	/*
	 * Registers a new player if game id exists, if not already registered and if not already 2 registered players present.
	 * 
	 * Returns a random player id wrapped in UniquePlayerIdentifier.
	 */
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration reg) {
		
		final String newPlayerId = this.controller
											.tryAddPerson(gameID.getUniqueGameID(), reg.getStudentFirstName(), reg.getStudentLastName(), reg.getStudentUAccount());	
		
		return new ResponseEnvelope<UniquePlayerIdentifier>(UniquePlayerIdentifier.of(newPlayerId));
	}
	
	/*
	 * Accepts a half map if game exists, if player on turn and if not already sent half map.
	 */
	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> receiveHalfMap(
				@Validated @PathVariable UniqueGameIdentifier gameID,
				@Validated @RequestBody PlayerHalfMap halfMap) {
		
		this.controller.tryAddHalfMap(gameID.getUniqueGameID(), halfMap);
		
		return new ResponseEnvelope<>();			
	}

	/*
	 * Returns data about a game if game exists and if player registered.
	 */
	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> receiveGetState(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @PathVariable UniquePlayerIdentifier playerID) {
		
		final GameState newGameState = this.controller.tryGetGameState(gameID.getUniqueGameID(), playerID.getUniquePlayerID());
		
		return new ResponseEnvelope<GameState>(newGameState);
	}
	
	/*
	 * Responsible for catching all errors.
	 * 
	 * When exception occurs one places the name of the exception + the message in the 
	 * response envelope and the exception gets handled by the client on the other side.
	 */
	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		final ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
	
}