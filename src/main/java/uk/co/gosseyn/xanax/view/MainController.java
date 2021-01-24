package uk.co.gosseyn.xanax.view;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.example.UnitMover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.gosseyn.xanax.facade.GameDataDto;
import uk.co.gosseyn.xanax.facade.GameFacade;
import uk.co.gosseyn.xanax.service.MapService;
import uk.co.gosseyn.xanax.domain.Tweet;
import uk.co.gosseyn.xanax.repository.MapRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

import static uk.co.gosseyn.xanax.domain.Map.DEPTH;
import static uk.co.gosseyn.xanax.domain.Map.HEIGHT;
import static uk.co.gosseyn.xanax.domain.Map.WIDTH;


@RestController
public class MainController {

    private static final double FEATURE_SIZE = 15;
    private final GameFacade gameFacade;
    @Inject
    public MainController(GameFacade gameFacade) {
        this.gameFacade = gameFacade;
    }
    @Autowired
    private MapService mapService;

    @Autowired
    private MapRepository gameData;

    private PathFinder finder;

    @RequestMapping("/gameData")
    public GameDataDto gameData(@RequestParam int left,
                                @RequestParam int top,
                                @RequestParam int z,
                                @RequestParam int width,
                                @RequestParam int height) {

        if (left < 0) {
            left = 0;
        } else if (left > WIDTH - width) {
            left = WIDTH - width;
        }
        if (top < 0) {
            top = 0;
        } else if (top > HEIGHT - height) {
            top = HEIGHT - height;
        }
        return gameFacade.getGameData(left, top, z);
    }

    @RequestMapping("/newMap")
    public void newMap() {
        gameData.setMap(mapService.newMap(WIDTH, HEIGHT, DEPTH, FEATURE_SIZE));
        finder = new AStarPathFinder(gameData.getMap(), 500, false);
        finder.findPath(new UnitMover(0),
                0, 4, 4, 15);
    }

    @RequestMapping("/twitter.json")
    public List<Tweet> twitterJson() {
        TwitterTemplate twitter = new TwitterTemplate(
                "XNKSEbcVXZqBhlmFaDyJasqGo",
                "0vrqpWCoh7qHoZvSSjLBtcABqtCtEOP7HIMCigrhbUgOrBGPiu",
                "907598957278351360-wKtQuetHdRI79tDrzkDoP2YQYnE9Zkh",
                "VmvE2ACgtMDgvcsCzJTU8S289N5BzQNpweRT2q48ZaI5e");


        return twitter.timelineOperations().getHomeTimeline(200).stream()
                .map(t -> Tweet.builder()
                        .user(t.getFromUser())
                        .time(interval(t.getCreatedAt().getTime()))
                        .text(t.getText())
                        .likes(t.getFavoriteCount())
                        .retweets(t.getRetweetCount())
                        .profileImage(t.getProfileImageUrl()).build()
                ).collect(Collectors.toList());
    }

    private String interval(long start) {
        long seconds = (new Date().getTime() - start) / 1000;
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            return (seconds / 60) + "m";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "h";
        } else {
            return (seconds / 86400) + "d";
        }
    }
}
