package uk.co.gosseyn.xanax.domain;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class TraversePathAction implements Action {

    @NonNull private List<Action> steps;

    @Override
    public List<Action> getSubActions() {
        return steps;
    }

    @Override
    public void perform() {

    }
}
