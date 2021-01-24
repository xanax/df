package uk.co.gosseyn.xanax;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
// TODO put in db
@Component
public class GameData {
    private Map map;
}
