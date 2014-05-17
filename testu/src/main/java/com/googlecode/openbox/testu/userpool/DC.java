package com.googlecode.openbox.testu.userpool;

public class DC {

	private String dcName;
	private int dcIndex;
	private int gsbIndex;

	DC(String dcName, int dcIndex, int gsbIndex) {
		this.dcName = dcName;
		this.dcIndex = dcIndex;
		this.gsbIndex = gsbIndex;
	}

	public static DC newInstance(String dcName, int dcIndex, int gsbIndex) {
		return new DC(dcName, dcIndex, gsbIndex);
	}

	public String getDcName() {
		return dcName;
	}

	public int getDcIndex() {
		return dcIndex;
	}

	public int getGsbIndex() {
		return gsbIndex;
	}

	public void setGsbIndex(int gsbIndex) {
		this.gsbIndex = gsbIndex;
	}

	@Override
	public String toString() {
		return dcName + "_primary" + dcIndex + "_gsb" + gsbIndex;
	}

	@Override
	public boolean equals(Object dc) {
		if (dc instanceof DC) {
			return toString().equals(dc.toString());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

}
