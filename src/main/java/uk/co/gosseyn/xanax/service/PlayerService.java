package uk.co.gosseyn.xanax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.gosseyn.xanax.domain.Player;
import uk.co.gosseyn.xanax.domain.SocialGroup;
import uk.co.gosseyn.xanax.repository.PlayerRepository;

import java.util.UUID;


@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private BitcoinService bitcoinService;

    public Player newPlayer() {
        Player player = new Player(bitcoinService.newAddress());
        player.getSocialGroups().add(new SocialGroup(player));
        savePlayer(player);
        return player;
    }
    public void savePlayer(Player player) {
        playerRepository.savePlayer(player);
    }
    public Player getPlayer(String playerId) {
        return playerRepository.getPlayer(playerId);
    }
}
