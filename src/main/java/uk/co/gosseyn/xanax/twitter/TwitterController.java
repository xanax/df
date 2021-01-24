package uk.co.gosseyn.xanax.twitter;

import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.gosseyn.xanax.domain.Tweet;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TwitterController {
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
