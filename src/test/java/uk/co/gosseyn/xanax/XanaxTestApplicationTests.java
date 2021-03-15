package uk.co.gosseyn.xanax;

import org.junit.jupiter.api.Test;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.Point;
import uk.co.gosseyn.xanax.domain.Vector2d;
import uk.co.gosseyn.xanax.service.GameService;
import uk.co.gosseyn.xanax.service.MapService;
import uk.co.gosseyn.xanax.service.PathFinderService;

import static java.util.stream.IntStream.range;
import static uk.co.gosseyn.xanax.domain.BlockMap.EMPTY;
import static uk.co.gosseyn.xanax.domain.BlockMap.GRASS;
import static uk.co.gosseyn.xanax.domain.BlockMap.ROCK;
import static uk.co.gosseyn.xanax.domain.BlockMap.TREE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class XanaxTestApplicationTests {

	@Autowired
	PathFinderService pathFinderService;

	@Test
	void contextLoads() {
	}

	@Test
	void pathfinder() {
		int[][][] mapData = {
				{
						{GRASS, GRASS, GRASS},
						{GRASS, ROCK, GRASS},
						{GRASS, GRASS, GRASS},
						{GRASS, GRASS, ROCK},
				},
				{
						{EMPTY, EMPTY, EMPTY},
						{EMPTY, GRASS, EMPTY},
						{EMPTY, GRASS, EMPTY},
						{EMPTY, EMPTY, EMPTY},
				},
				{
						{EMPTY, EMPTY, EMPTY},
						{EMPTY, EMPTY, EMPTY},
						{EMPTY, GRASS, EMPTY},
						{EMPTY, EMPTY, EMPTY},
				},
		};
		BlockMap map = new BlockMap(
				new Point(mapData[0][0].length, mapData[0].length, mapData.length));

		range(0, mapData.length).forEach(z ->
			range(0, mapData[0].length).forEach(y ->
				range(0, mapData[0][0].length).forEach(x ->
					map.setBlock(new Point(x,y,z), mapData[z][y][x]))));
		Path path = pathFinderService.findPath(map, new Point(1,2,1),
				new Point(1,2,2), true, false);
		System.out.print(path);
	}
}
