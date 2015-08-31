package com.googlecode.openbox.phone;

import net.sourceforge.peers.media.AbstractSoundManager;

public class LinuxDtmfSuccessSoundManager extends AbstractSoundManager {

	public LinuxDtmfSuccessSoundManager() {
	}

	@Override
	public synchronized byte[] readData() {
		return new byte[1];
	}

	@Override
	public int writeData(byte[] buffer, int offset, int length) {
		return length;
	}

	@Override
	public void init() {

	}

	@Override
	public void close() {
	}
}