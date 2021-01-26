package uk.co.gosseyn.xanax.service;

import lombok.var;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Player;


@Service
public class PlayerService {
    public Player newPlayer() {
        var player = new Player();
        return player;
    }
}
