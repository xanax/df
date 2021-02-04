package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class SocialGroup implements HasZones {
    HasGroups owner;
    Collection<Zone> zones = new ArrayList<>();
    Collection<CanJoinSocialGroup> members = new ArrayList<>();
    Collection<Task> tasks = new ArrayList<>();

}
