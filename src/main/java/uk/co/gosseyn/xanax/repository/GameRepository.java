package uk.co.gosseyn.xanax.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.co.gosseyn.xanax.domain.Game;

@Getter
@Setter
// TODO put in db
@Component
public class GameRepository {
    private Game game = new Game();
}
