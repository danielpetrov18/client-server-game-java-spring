package client.map.generator;

import java.text.MessageFormat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import client.map.EMyTerrain;
import client.map.halfmap.MyPlayerHalfMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapGeneratorTests {

	final private static long MIN_WATER = 7;
	final private static long MIN_GRASS = 24;
	final private static long MIN_MOUNTAINS = 5;
	final private static long EXPECTED_CASTLE_COUNT = 1;
	
	private static HalfmapGenerator generator;
	private static MyPlayerHalfMap halfMap;
	
	@BeforeAll
	public static void initMapGenerator() {
		generator = new HalfmapGenerator();
	}
	
	// Left out the static modifier because it does not compile otherwise.
	@BeforeEach
	public void createNewHalfMap() {
		halfMap = generator.generateMap();
	}
	
	@Test
	public void GeneratingMap_CastlePlacedAtRandom_ExpectedOnlyOneCastle() {
		long castleCount = halfMap
						.getMapNodes()
						.stream()
						.filter(node -> node.isFortPresent())
						.count();
		
		assertEquals(castleCount, EXPECTED_CASTLE_COUNT, MessageFormat.format("Expected {1} castle. Got {2}.", EXPECTED_CASTLE_COUNT, castleCount));
	}
	
	@Test
	public void GeneratingMap_CastlePlacedAtRandom_ExpectedCastleOnGrassField() {
		final EMyTerrain expectedTerrain = EMyTerrain.GRASS;
		final EMyTerrain actualTerrain = halfMap
											.getMapNodes()
											.stream()
											.filter(node -> node.isFortPresent())
											.findFirst()
											.get()
											.getTerrain();
		
		Assertions.assertTrue(expectedTerrain.equals(actualTerrain));
	} 
	
	@Test
	public void GeneratingMap_PlacedNodeTypesAtRandom_ExpectedEnoughNodesOfEachType() {
		long actualMountains = halfMap.getMapNodes().stream().filter(n -> n.getTerrain().equals(EMyTerrain.MOUNTAIN)).count();
		long actualGrass = halfMap.getMapNodes().stream().filter(n -> n.getTerrain().equals(EMyTerrain.GRASS)).count();
		long actualWater = halfMap.getMapNodes().stream().filter(n -> n.getTerrain().equals(EMyTerrain.WATER)).count();
		
		Assertions.assertTrue(actualMountains >= MIN_MOUNTAINS);
		Assertions.assertTrue(actualGrass >= MIN_GRASS);
		Assertions.assertTrue(actualWater >= MIN_WATER);
	}
}
