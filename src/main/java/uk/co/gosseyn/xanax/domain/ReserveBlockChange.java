package uk.co.gosseyn.xanax.domain;

import org.newdawn.slick.util.pathfinding.Path;

public class ReserveBlockChange extends Change {
    MineTask task;
    Point point;
    public ReserveBlockChange( final MineTask mineTask, final Point point) {
        super();
        this.point = point;
        this.task = mineTask;
    }

    @Override
    public void perform(final Game game) {
        task.getReserved().add(point);
    }
}
