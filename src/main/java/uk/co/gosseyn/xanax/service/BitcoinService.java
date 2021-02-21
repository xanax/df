package uk.co.gosseyn.xanax.service;

import org.bitcoinj.core.Address;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.Wallet;
import uk.co.gosseyn.xanax.domain.GameObject;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Named
public class BitcoinService {

    private Wallet wallet;

    @PostConstruct
    public void init() throws IOException {
        wallet = Wallet.createDeterministic(MainNetParams.get(), Script.ScriptType.P2PKH);
        String privateKey = wallet.getWatchingKey().serializePrivB58(MainNetParams.get());
        Files.write(Paths.get("private-key.txt"), (privateKey+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public String newAddress() {
        return wallet.freshReceiveAddress().toString();
    }
}
