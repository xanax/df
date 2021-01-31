package uk.co.gosseyn.xanax.domain;

import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

@Data
public class MoveMapItemAction implements Action {
    @NonNull
    private Point from;
    @NonNull
    private Point offset;

    @Override
    public List<Action> getSubActions() {
        return Collections.emptyList();
    }

    @Override
    public void perform() {

    }
}
