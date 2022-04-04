package edu.nyu.crypto.miners;

import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

public class SelfishMiner extends CompliantMiner implements Miner {
	protected Block currentHead;
	protected Block secretBlock;
	protected double myShare;
	protected boolean selfishMining = false;

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
		if(isMinerMe) {
			if (block.getHeight() > secretBlock.getHeight()) {
				this.secretBlock = block;
				selfishMining = true;
			}
		}
		else {
			int lead = secretBlock.getHeight() - block.getHeight();
			if (selfishMining) {
				if (lead > 1) {}
				else if (lead == 0 || lead == 1) {
					this.currentHead = secretBlock;
					selfishMining = false;
				}
				else {
					this.secretBlock = block;
					this.currentHead = block;
					selfishMining = false;
				}
			}
			else if (block.getHeight() > currentHead.getHeight()) {
				this.currentHead = block;
				this.secretBlock = block;
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
