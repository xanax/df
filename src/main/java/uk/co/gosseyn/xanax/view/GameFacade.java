package uk.co.gosseyn.xanax.view;

import uk.co.gosseyn.xanax.domain.Bounds;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.view.web.FrameData;

public interface GameFacade {
    FrameData getFrameData(Game game, Bounds bounds);
}
