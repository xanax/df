package uk.co.gosseyn.xanax;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.example.UnitMover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

import static java.util.Collections.singletonMap;
import static uk.co.gosseyn.xanax.Map.DEPTH;
import static uk.co.gosseyn.xanax.Map.HEIGHT;
import static uk.co.gosseyn.xanax.Map.WIDTH;


@RestController
public class MainController {

    private static final double FEATURE_SIZE = 15;

    @Autowired
    private MapService mapService;

    @Autowired
    private GameData gameData;

    private PathFinder finder;

    @RequestMapping("/gameData")
    public GameDataView gameData(@RequestParam int left,
                                 @RequestParam int top,
                                 @RequestParam int z,
                                 @RequestParam int width,
                                 @RequestParam int height) {

        if(left < 0) {left = 0;}
        else if(left > WIDTH - width) {
            left = WIDTH - width;
        }
        if(top < 0) {top = 0;}
        else if(top > HEIGHT - height) {
            top = HEIGHT - height;
        }
//        if(z<1){z = 1;}
//        else if (z> DEPTH - 2) {
//            z = DEPTH - 2;
//        }
        GameDataView viewModel = new GameDataView();
        viewModel.setMap(new List[width][height]);

        for(int y = top; y < top + height; y++) {
            for(int x = left; x < left + width; x++) {
                int view;

                int current = gameData.getMap().getMap()[x][y][z];
                if (current != 0 && (z == DEPTH - 1 || gameData.getMap().getMap()[x][y][z + 1] == 0)) {
                    // current block full and nothing above
                    view = current;
                } else if (current == 0 && gameData.getMap().getMap()[x][y][z - 1] != 0) {
                    // current empty and below contains block
                    view = gameData.getMap().getMap()[x][y][z - 1] + 100;
                } else if (current == 0) {
                    // just space
                    view = -1;
                } else {
                    // embedded
                    view = 0;
                }
                viewModel.getMap()[x - left][y - top] = new ArrayList<>();

            viewModel.getMap()[x - left][y - top].add(view);
                if(gameData.getMap().getItemsMap()[x][y][z] != null) {
                    // TODO
                    viewModel.getMap()[x - left][y - top].addAll(singleton(gameData.getMap().getItemsMap()[x][y][z].iterator().next().getCode()));
                }
                if(gameData.getMap().getItemsMap()[x][y][z+1] != null) {
                    // TODO
                    viewModel.getMap()[x - left][y - top].addAll(singleton(gameData.getMap().getItemsMap()[x][y][z+1].iterator().next().getCode()));
                }

        }
        }
        return viewModel;
    }

    @RequestMapping("/newMap")
    public void newMap() {
        gameData.setMap(mapService.newMap(WIDTH, HEIGHT, DEPTH, FEATURE_SIZE));
        finder = new AStarPathFinder(gameData.getMap(), 500, false);
//        finder.findPath(new UnitMover(99),
//                selectedx, selectedy, x, y);
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
        if(seconds < 60) {
            return seconds+"s";
        } else if(seconds < 3600) {
            return (seconds / 60)+"m";
        } else if(seconds < 86400) {
            return (seconds / 3600)+"h";
        } else {
            return (seconds / 86400)+"d";
        }
    }
}
