package uk.co.gosseyn.xanax;

import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.FullPrunedBlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.FullPrunedBlockStore;
import org.bitcoinj.store.MemoryFullPrunedBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@Slf4j
public class BitcoinTest {

    @Test
    public void testSumint() throws BlockStoreException {
        NetworkParameters netParams;
        Wallet wallet = Wallet.createDeterministic(MainNetParams.get(), Script.ScriptType.P2PKH);
        MainNetParams params = MainNetParams.get();
//        FullPrunedBlockStore blockStore = new MemoryFullPrunedBlockStore(params, 10);
//        FullPrunedBlockChain blockChain = new FullPrunedBlockChain(params, wallet, blockStore);
//        PeerGroup peerGroup = new PeerGroup(params, blockChain);
//        peerGroup.addWallet(wallet);
////        peerGroup.startAndWait();
//        peerGroup.start();
        final AtomicReference<Address> address = new AtomicReference<Address>();
        IntStream.range(0 ,1000).forEach(t -> {
            address.set(wallet.freshReceiveAddress());
            System.out.println("Public key: " + address.toString());
            System.out.println("Private key: "+ wallet.findKeyFromAddress(address.get()).getPrivateKeyEncoded(params));
        });

        // Master and private key
        wallet.getWatchingKey().serializePrivB58(params);
    }
}
