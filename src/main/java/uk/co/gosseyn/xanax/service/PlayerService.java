package uk.co.gosseyn.xanax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Bounds;
import uk.co.gosseyn.xanax.domain.Player;
import uk.co.gosseyn.xanax.domain.SocialGroup;
import uk.co.gosseyn.xanax.repository.PlayerRepository;
import uk.co.gosseyn.xanax.view.GameFacade;
import uk.co.gosseyn.xanax.view.web.FrameData;

import java.util.UUID;


@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private IdService bitcoinService;

    public Player newPlayer(GameFacade gameFacade) {
        Player player = new Player(bitcoinService.newAddress());
        player.setGameFacade(gameFacade);
        player.getSocialGroups().add(new SocialGroup(player));
        savePlayer(player);
        return player;
    }
    public FrameData getCurrentFrameData(Player player, Bounds bounds) {
        FrameData frameData = player.getGameFacade().getFrameData(player.getGame(), bounds);
        player.setLastUpdate(System.currentTimeMillis());
        return frameData;
    }

    public void savePlayer(Player player) {
        playerRepository.savePlayer(player);
    }
    public Player getPlayer(String playerId) {
        return playerRepository.getPlayer(playerId);
    }
}
