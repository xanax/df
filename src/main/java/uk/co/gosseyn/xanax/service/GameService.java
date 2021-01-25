package uk.co.gosseyn.xanax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Game;
import uk.co.gosseyn.xanax.repository.GameRepository;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;
    public Game getGame() {
        return gameRepository.getGame();
    }

    public void update() {

    }

}
