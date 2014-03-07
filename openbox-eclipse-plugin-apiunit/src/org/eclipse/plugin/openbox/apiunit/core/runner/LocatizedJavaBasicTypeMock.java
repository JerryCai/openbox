package org.eclipse.plugin.openbox.apiunit.core.runner;


public enum LocatizedJavaBasicTypeMock {

	BOOLEAN(JavaBasicTypes.BOOLEAN, JavaBasicTypes.JAVA_LANG_BOOLEAN),
	BYTE(JavaBasicTypes.BYTE,JavaBasicTypes.JAVA_LANG_BYTE), 
	CHAR(JavaBasicTypes.CHAR, JavaBasicTypes.JAVA_LANG_CHARACTER), 
	DOUBLE(JavaBasicTypes.DOUBLE, JavaBasicTypes.JAVA_LANG_DOUBLE), 
	FLOAT(JavaBasicTypes.FLOAT,JavaBasicTypes.JAVA_LANG_FLOAT), 
	INT(JavaBasicTypes.INT, JavaBasicTypes.JAVA_LANG_INTEGER), 
	LONG(JavaBasicTypes.LONG, JavaBasicTypes.JAVA_LANG_LONG), 
	SHORT(JavaBasicTypes.SHORT,JavaBasicTypes.JAVA_LANG_SHORT);
	
	
	private String basicTypeName;
	private String basicTypeWrapperName;
	
	private LocatizedJavaBasicTypeMock(String basicTypeName, String basicTypeWrapperName) {
		this.basicTypeName = basicTypeName;
		this.basicTypeWrapperName = basicTypeWrapperName;
	}

	
	public static LocatizedJavaBasicTypeMock fromValue(String basicType) {
		LocatizedJavaBasicTypeMock[] javaBasicTypes = LocatizedJavaBasicTypeMock.values();
		for (LocatizedJavaBasicTypeMock javaBasicType : javaBasicTypes) {
			if (javaBasicType.getBasicTypeName().equals(basicType)) {
				return javaBasicType;
			}
		}
		return null;
	}



	public String getBasicTypeName() {
		return basicTypeName;
	}



	public void setBasicTypeName(String basicTypeName) {
		this.basicTypeName = basicTypeName;
	}



	public String getBasicTypeWrapperName() {
		return basicTypeWrapperName;
	}



	public void setBasicTypeWrapperName(String basicTypeWrapperName) {
		this.basicTypeWrapperName = basicTypeWrapperName;
	}
}
