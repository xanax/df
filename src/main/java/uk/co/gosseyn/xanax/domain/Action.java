package uk.co.gosseyn.xanax.domain;

import java.util.List;

public interface Action {
    List<Action> getSubActions();
    void perform();

}
