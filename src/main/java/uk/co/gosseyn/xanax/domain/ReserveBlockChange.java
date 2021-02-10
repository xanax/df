package uk.co.gosseyn.xanax.domain;

public class ReserveBlockChange extends Change {
    ForestTask task;
    Point point;
    public ReserveBlockChange(final ForestTask forestTask, final Point point) {
        super();
        this.point = point;
        this.task = forestTask;
    }

    @Override
    public void perform(final Game game) {
        task.getReserved().add(point);
    }
}
