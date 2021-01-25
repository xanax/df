package uk.co.gosseyn.xanax.view.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.gosseyn.xanax.domain.Map;
import uk.co.gosseyn.xanax.domain.Vector3d;
import uk.co.gosseyn.xanax.service.GameService;
import uk.co.gosseyn.xanax.service.MapService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singleton;

@Component
public class GameFacade {
    @Autowired
    private GameService gameService;

    public GameDataDto getGameData(int xTileOffset, int yTileOffset, int zTileOffset, int screenWidthInTiles, int screenHeightInTiles) {
        GameDataDto gameDataDto = new GameDataDto();
        gameDataDto.setMap(new List[screenWidthInTiles][screenHeightInTiles]);

        Map map = gameService.getGame().getMap();
        for (int y = yTileOffset; y < yTileOffset + screenHeightInTiles; y++) {
            for (int x = xTileOffset; x < xTileOffset + screenWidthInTiles; x++) {
                int view;

                int current = map.getBlock(new Vector3d(x, y, zTileOffset));

                if (current != 0 && (zTileOffset == map.getDepthInTiles() - 1 ||
                        map.getBlock(new Vector3d(x, y, zTileOffset +1)) == 0)) {
                    // current block full and nothing above
                    view = current;
                } else if (current == 0 && map.getBlock(new Vector3d(x, y, zTileOffset - 1)) != 0) {
                    // current empty and below contains block
                    view = map.getBlock(new Vector3d(x, y, zTileOffset - 1)) + 100;
                } else if (current == 0) {
                    // just space
                    view = -1;
                } else {
                    // embedded
                    view = 0;
                }
                gameDataDto.getMap()[x - xTileOffset][y - yTileOffset] = new ArrayList<>();

                gameDataDto.getMap()[x - xTileOffset][y - yTileOffset].add(view);
                Vector3d location = new Vector3d(x, y, zTileOffset);
                if (!map.getItem(location).isEmpty()) {
                    // TODO
                    gameDataDto.getMap()[x - xTileOffset][y - yTileOffset]
                            .addAll(singleton(map.getItem(location).iterator().next().getCode()));
                }
                location = new Vector3d(x, y, zTileOffset -1);
                if (zTileOffset > 1 && !map.getItem(location).isEmpty()) {
                    // TODO
                    gameDataDto.getMap()[x - xTileOffset][y - yTileOffset]
                            .addAll(singleton(map.getItem(location).iterator().next().getCode()));
                }

            }
        }
        return gameDataDto;
    }
}
