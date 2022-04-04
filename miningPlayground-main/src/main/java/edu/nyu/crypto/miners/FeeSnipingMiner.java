package edu.nyu.crypto.miners;


import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

import java.util.ArrayList;
import java.util.Collections;

public class FeeSnipingMiner extends CompliantMiner implements Miner {
    protected Block currentHead;
    protected double myShare;
//    protected ArrayList<Double> values = new ArrayList<>();
//    protected double medium;

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
//        if (values.size() < 2000){
//            values.add(block.getBlockValue());
//            Collections.sort(values);
//            medium = values.get(values.size()/2);
//        } else {
//            values.clear();
//        }

        if(isMinerMe) {
            if (block.getHeight() > currentHead.getHeight()) {
                this.currentHead = block;
            }
        }
        else {
            if (block.getHeight() > currentHead.getHeight()) {
                if (block.getBlockValue() < 8) {
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
