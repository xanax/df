package uk.co.gosseyn.xanax.facade;

import org.springframework.stereotype.Component;
import uk.co.gosseyn.xanax.domain.Map;
import uk.co.gosseyn.xanax.service.MapService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singleton;
import static uk.co.gosseyn.xanax.domain.Map.DEPTH;

@Component
public class GameFacade {
    private int screenWidthInTiles = 24;
    private int screenHeightInTiles = 20;
    private MapService mapService;
    @Inject
    public GameFacade(MapService mapService) {
        this.mapService = mapService;
    }
    public GameDataDto getGameData(int xTileOffset, int yTileOffset, int zTileOffset) {
        GameDataDto gameDataDto = new GameDataDto();
        gameDataDto.setMap(new List[screenWidthInTiles][screenHeightInTiles]);

        Map map = mapService.getMap();
        for (int y = yTileOffset; y < yTileOffset + screenHeightInTiles; y++) {
            for (int x = xTileOffset; x < xTileOffset + screenWidthInTiles; x++) {
                int view;

                int current = map.getMap()[x][y][zTileOffset];
                if (current != 0 && (zTileOffset == DEPTH - 1 || map.getMap()[x][y][zTileOffset + 1] == 0)) {
                    // current block full and nothing above
                    view = current;
                } else if (current == 0 && map.getMap()[x][y][zTileOffset - 1] != 0) {
                    // current empty and below contains block
                    view = map.getMap()[x][y][zTileOffset - 1] + 100;
                } else if (current == 0) {
                    // just space
                    view = -1;
                } else {
                    // embedded
                    view = 0;
                }
                gameDataDto.getMap()[x - xTileOffset][y - yTileOffset] = new ArrayList<>();

                gameDataDto.getMap()[x - xTileOffset][y - yTileOffset].add(view);
                if (map.getItemsMap()[x][y][zTileOffset] != null) {
                    // TODO
                    gameDataDto.getMap()[x - xTileOffset][y - yTileOffset]
                            .addAll(singleton(map.getItemsMap()[x][y][zTileOffset].iterator().next().getCode()));
                }
                if (zTileOffset < DEPTH - 1 && map.getItemsMap()[x][y][zTileOffset + 1] != null) {
                    // TODO
                    gameDataDto.getMap()[x - xTileOffset][y - yTileOffset]
                            .addAll(singleton(map.getItemsMap()[x][y][zTileOffset + 1].iterator().next().getCode()));
                }

            }
        }
        return gameDataDto;
    }
}
