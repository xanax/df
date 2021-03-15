package uk.co.gosseyn.xanax.service;

import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.Wallet;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Named
@Profile("!bitcoin")
public class SimpleIdService implements IdService {

    public String newAddress() {
        return UUID.randomUUID().toString();
    }
}
