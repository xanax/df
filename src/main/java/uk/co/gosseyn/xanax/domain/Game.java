package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bitcoinj.wallet.Wallet;
import uk.co.gosseyn.xanax.service.WalletHolder;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@Builder
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
//@Entity
public class Game {

    //@Id
    String gameId;
    @NonFinal
    @Builder.Default
    BigInteger frame = BigInteger.ZERO;

    BlockMap map;
    List<Change> changes = new ArrayList<>();



    List<Active> activeItems = new ArrayList<>();
    List<Action> actionLog = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    Collection<SocialGroup> socialGroups = new ArrayList<>();


     List<Task> tasks = new ArrayList<>();
     List<TaskAssignment> taskAssignments = new ArrayList<>();


}
