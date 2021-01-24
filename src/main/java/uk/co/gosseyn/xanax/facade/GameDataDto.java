package uk.co.gosseyn.xanax.facade;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class GameDataDto {
    private List<Integer> map[][];
}
