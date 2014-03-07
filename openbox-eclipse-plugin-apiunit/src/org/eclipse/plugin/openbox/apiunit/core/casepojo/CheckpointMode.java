package org.eclipse.plugin.openbox.apiunit.core.casepojo;

public enum CheckpointMode {
	RETURN_OBJECT_MODE(0), 
	CHECKPOINT_API_WITHOUTINPUT_MODE(1),
	CHECKPOINT_API_WITHINPUT_MODE(2);

	private int mode;

	private CheckpointMode(int mode) {
		this.mode = mode;
	}
	public static CheckpointMode parseMode(String mode) {
		int intMode = Integer.parseInt(mode);
		return parseMode(intMode);
	}
	public static CheckpointMode parseMode(int mode) {
		switch (mode%3) {
		case 0:
			return RETURN_OBJECT_MODE;
		case 1:
			return CHECKPOINT_API_WITHOUTINPUT_MODE;
		case 2:
			return CHECKPOINT_API_WITHINPUT_MODE;
		default:
			return null;
		}
	}

	public int toValue() {
		return this.mode;
	}

}
