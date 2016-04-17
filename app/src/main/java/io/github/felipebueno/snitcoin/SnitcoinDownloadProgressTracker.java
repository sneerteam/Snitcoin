package io.github.felipebueno.snitcoin;

import com.google.common.util.concurrent.SettableFuture;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.DownloadProgressTracker;
import org.bitcoinj.core.FilteredBlock;
import org.bitcoinj.core.Peer;

import javax.annotation.Nullable;

public class SnitcoinDownloadProgressTracker extends DownloadProgressTracker {
    private int originalBlocksLeft = -1;
    private int lastPercent = 0;
    private SettableFuture<Long> future = SettableFuture.create();
    private boolean caughtUp = false;

    @Override
    public void onChainDownloadStarted(Peer peer, int blocksLeft) {
        super.onChainDownloadStarted(peer, blocksLeft);
//        Log.i(TAG, "SnitcoinDownloadProgressTracker - Starting blockchain download " + blocksLeft);
    }

    @Override
    public void onBlocksDownloaded(Peer peer, Block block, @Nullable FilteredBlock filteredBlock, int blocksLeft) {
        super.onBlocksDownloaded(peer, block, filteredBlock, blocksLeft);
        double pct = 100.0 - (100.0 * (blocksLeft / (double) originalBlocksLeft));
        if ((int) pct != lastPercent) {
//            Log.i(TAG, "SnitcoinDownloadProgressTracker " + lastPercent + " " + blocksLeft);
            lastPercent = (int) pct;
        }
    }

    protected void doneDownload() {
//        Log.i(TAG, "SnitcoinDownloadProgressTracker - Blockchain download done!");
    }
}
