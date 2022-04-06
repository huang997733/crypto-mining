package edu.nyu.crypto.miners;


import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;


public class FeeSnipingMiner extends CompliantMiner implements Miner {
    protected Block currentHead;
    protected double myShare;
    protected int minimumValue = 8;

    public FeeSnipingMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);

    }

	// TODO Override methods to implement Fee Sniping

    @Override
    public Block currentlyMiningAt() {
        return currentHead;
    }

    @Override
    public Block currentHead() {
        return currentHead;
    }

    @Override
    public void blockMined(Block block, boolean isMinerMe) {

        if(isMinerMe) {
            if (block.getHeight() > currentHead.getHeight()) {
                this.currentHead = block;
            }
        }
        else {
            if (block.getHeight() > currentHead.getHeight()) {
                if (block.getBlockValue() < minimumValue) {
                    this.currentHead = block;
                } else {
                    if (block.getPreviousBlock() != null) {
                        this.currentHead = block.getPreviousBlock();
                    } else {
                        this.currentHead = block;
                    }
                }
            }
        }
    }


    @Override
    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.currentHead = genesis;
    }

    @Override
    public void networkUpdate(NetworkStatistics statistics) {
        myShare = (double) this.getHashRate() / statistics.getTotalHashRate();
    }
}
