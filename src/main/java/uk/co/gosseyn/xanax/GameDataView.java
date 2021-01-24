package uk.co.gosseyn.xanax;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class GameDataView {
    private List<Integer> map[][];
}
