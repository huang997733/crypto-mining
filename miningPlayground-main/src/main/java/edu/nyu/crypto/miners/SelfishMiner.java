package edu.nyu.crypto.miners;

import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

public class SelfishMiner extends CompliantMiner implements Miner {
	protected Block currentHead;
	protected Block secretBlock;
	protected double myShare;

	public SelfishMiner(String id, int hashRate, int connectivity) {
		super(id, hashRate, connectivity);
	}
   
	// TODO Override methods to implement Selfish Mining

	@Override
	public Block currentlyMiningAt() {
		return secretBlock;
	}

	@Override
	public Block currentHead() {
		return currentHead;
	}

	@Override
	public void blockMined(Block block, boolean isMinerMe) {
		if (myShare > 0.25) {
			if(isMinerMe) {
				if (block.getHeight() > secretBlock.getHeight()) {
					this.secretBlock = block;
				}
			}
			else {
				if (block.getHeight() > currentHead.getHeight()) {
					currentHead = block;
					int lead = secretBlock.getHeight() - block.getHeight();
					if (lead == 1 || lead == 0) {
						currentHead = secretBlock;
					}
					if (lead < 0) {
						secretBlock = currentHead;
					}
				}
			}
		}
		else {
			if (isMinerMe) {
				if (block.getHeight() > secretBlock.getHeight()) {
					secretBlock = block;
					currentHead = block;
				}
			}
			else{
				if (block.getHeight() > secretBlock.getHeight()) {
					secretBlock = block;
					currentHead = block;
				}
			}
		}
	}

	@Override
	public void initialize(Block genesis, NetworkStatistics networkStatistics) {
		this.currentHead = genesis;
		this.secretBlock = genesis;
	}

	@Override
	public void networkUpdate(NetworkStatistics statistics) {
		myShare = (double) this.getHashRate() / statistics.getTotalHashRate();
	}

}
