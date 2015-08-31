package com.googlecode.openbox.phone;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.sourceforge.peers.Logger;
import net.sourceforge.peers.media.AbstractSoundManager;

public class LinuxFileSoundManager extends AbstractSoundManager {
	private Logger LOGGER;

	private File writeFile;
	private File readFile;
	private FileWriter fileWriter;
	private FileReader fileReader;

	public LinuxFileSoundManager(Logger LOGGER) {
		this.LOGGER = LOGGER;
	}

	@Override
	public synchronized byte[] readData() {
		return new byte[1];
	}

	@Override
	public void init() {
		try {
			this.readFile = File.createTempFile("temp_javasoftphone_reader_",
					".phone");
			this.writeFile = File.createTempFile("temp_javasoftphone_writer_",
					".phone");
			this.fileReader = new FileReader(this.readFile);
			this.fileWriter = new FileWriter(this.writeFile);
		} catch (IOException e) {
			LOGGER.error("create LinuxFileSoundManager Writer error", e);
			throw new PhoneException(
					"create LinuxFileSoundManager Writer error", e);
		}

	}

	@Override
	public void close() {
		if (null != fileReader) {
			try {
				fileReader.close();
			} catch (Exception e) {
				LOGGER.error("close error", e);
			}
		}
		if (null != fileWriter) {
			try {
				fileWriter.close();
			} catch (IOException e) {
				LOGGER.error("close error", e);
			}
		}
		if (null != writeFile) {
			writeFile.deleteOnExit();
		}
	}

	@Override
	public int writeData(byte[] buffer, int offset, int length) {
		String data = new String(buffer, offset, length);
		try {
			fileWriter.write(data.toCharArray());
			fileWriter.flush();
		} catch (Exception e) {
			LOGGER.error("write data [" + data + "] error", e);
		}
		return length;
	}

}
