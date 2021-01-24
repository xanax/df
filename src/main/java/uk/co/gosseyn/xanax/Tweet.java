package uk.co.gosseyn.xanax;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Tweet {
    String user;
    String time;
    String text;
    int retweets;
    int likes;
    String profileImage;
}
