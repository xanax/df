package uk.co.gosseyn.xanax.domain;

import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

@Data
public class MoveMapItemAction implements Action {
    @NonNull
    private Vector3d from;
    @NonNull
    private Vector3d offset;

    @Override
    public List<Action> getSubActions() {
        return Collections.emptyList();
    }

    @Override
    public void perform() {

    }
}
