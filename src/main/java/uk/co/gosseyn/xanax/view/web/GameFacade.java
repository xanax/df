package uk.co.gosseyn.xanax.view.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.domain.BlockMap;
import uk.co.gosseyn.xanax.domain.Locatable;
import uk.co.gosseyn.xanax.domain.Nameable;
import uk.co.gosseyn.xanax.domain.Point;
import uk.co.gosseyn.xanax.service.GameService;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singleton;

@Component
public class GameFacade {
    @Autowired
    private GameService gameService;

    public FrameData getFrameData(Game game, int xTileOffset, int yTileOffset, int zTileOffset, int screenWidthInTiles, int screenHeightInTiles) {
        FrameData frameData = new FrameData();
        frameData.setTiles(new int[screenWidthInTiles*screenHeightInTiles]);
        frameData.setHeights(new int[screenWidthInTiles*screenHeightInTiles]);

        BlockMap map = game.getMap();

        if (xTileOffset < 0) {
            xTileOffset = 0;
        } else if (xTileOffset > map.getWidthInTiles() - screenWidthInTiles) {
            xTileOffset = map.getWidthInTiles() - screenWidthInTiles;
        }
        if (yTileOffset < 0) {
            yTileOffset = 0;
        } else if (yTileOffset > map.getHeightInTiles() - screenHeightInTiles) {
            yTileOffset = map.getHeightInTiles() - screenHeightInTiles;
        }

        for (int y = yTileOffset; y < yTileOffset + screenHeightInTiles; y++) {
            for (int x = xTileOffset; x < xTileOffset + screenWidthInTiles; x++) {
                int screenTileX = x - xTileOffset;
                int screenTileY = y - yTileOffset;
                int screenTileIndex = screenTileY * screenWidthInTiles + screenTileX;
                Point point = new Point(x, y, zTileOffset);
                while(map.getBlockNumber(point) == BlockMap.EMPTY && map.getItem(point).isEmpty() && point.getZ() > 0) {
                    point.addz(-1);
                }
                if(point.getZ() == 0) {
                    frameData.getTiles()[screenTileIndex] = map.getBlockNumber(new Point(x, y, zTileOffset));
                } else if(!map.getItem(point).isEmpty()) {
                    frameData.getTiles()[screenTileIndex] = map.getItem(point).iterator().next().getCode();
                    Locatable locatable = map.getItem(point).iterator().next();
                    if(locatable instanceof Nameable) {
                        BlockData blockData = new BlockData();
                        Nameable nameable = (Nameable)locatable;
                        blockData.setName(nameable.getName());
                        frameData.getBlockData().put((point.getX() - xTileOffset)+"-"+(point.getY() - yTileOffset)+"-"+point.getZ(),
                            blockData);
                    }
                } else {
                    frameData.getTiles()[screenTileIndex] = map.getBlockNumber(point);
                }
                frameData.getHeights()[screenTileIndex] = point.getZ();
            }
        }
        return frameData;
    }
}
