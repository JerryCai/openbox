package org.eclipse.plugin.openbox.apiunit.core.runner;

public enum JavaBasicTypeProxy {
	BOOLEAN(JavaBasicTypes.BOOLEAN, boolean.class), 
	BYTE(JavaBasicTypes.BYTE,byte.class), 
	CHAR(JavaBasicTypes.CHAR, char.class), 
	DOUBLE(JavaBasicTypes.DOUBLE, double.class), 
	FLOAT(JavaBasicTypes.FLOAT,float.class), 
	INT(JavaBasicTypes.INT, int.class), 
	LONG(JavaBasicTypes.LONG, long.class), 
	SHORT(JavaBasicTypes.SHORT,short.class),
	
//	BOOLEAN(JavaBasicTypes.BOOLEAN, java.lang.Boolean.class),
//	BYTE(JavaBasicTypes.BYTE,java.lang.Byte.class), 
//	CHAR(JavaBasicTypes.CHAR, java.lang.Character.class), 
//	DOUBLE(JavaBasicTypes.DOUBLE, java.lang.Double.class), 
//	FLOAT(JavaBasicTypes.FLOAT,java.lang.Float.class), 
//	INT(JavaBasicTypes.INT, java.lang.Integer.class), 
//	LONG(JavaBasicTypes.LONG, java.lang.Long.class), 
//	SHORT(JavaBasicTypes.SHORT,java.lang.Short.class),
	
	JAVA_LANG_BYTE(JavaBasicTypes.JAVA_LANG_BYTE,java.lang.Byte.class),
	JAVA_LANG_SHORT(JavaBasicTypes.JAVA_LANG_SHORT,java.lang.Short.class),
	JAVA_LANG_INTEGER(JavaBasicTypes.JAVA_LANG_INTEGER,java.lang.Integer.class),
	JAVA_LANG_LONG(JavaBasicTypes.JAVA_LANG_LONG,java.lang.Long.class),
	JAVA_LANG_FLOAT(JavaBasicTypes.JAVA_LANG_FLOAT,java.lang.Float.class),
	JAVA_LANG_DOUBLE(JavaBasicTypes.JAVA_LANG_DOUBLE,java.lang.Double.class),
	JAVA_LANG_CHARACTER(JavaBasicTypes.JAVA_LANG_CHARACTER,java.lang.Character.class),
	JAVA_LANG_BOOLEAN(JavaBasicTypes.JAVA_LANG_BOOLEAN,java.lang.Boolean.class),

	JAVA_LANG_STRING(JavaBasicTypes.JAVA_LANG_STRING,java.lang.String.class);

	private String basicType;
	private Class<?> basicTypeClass;

	private JavaBasicTypeProxy(String basicType, Class<?> basicTypeClass) {
		this.basicType = basicType;
		this.basicTypeClass = basicTypeClass;
	}

	public String getBasicType() {
		return basicType;
	}

	public void setBasicType(String basicType) {
		this.basicType = basicType;
	}

	public Class<?> getBasicTypeClass() {
		return basicTypeClass;
	}

	public void setBasicTypeClass(Class<?> basicTypeClass) {
		this.basicTypeClass = basicTypeClass;
	}

	public static boolean isBasicType(String className){
		JavaBasicTypeProxy[] javaBasicTypes = JavaBasicTypeProxy.values();
		for (JavaBasicTypeProxy javaBasicType : javaBasicTypes) {
			if (javaBasicType.getBasicType().equals(className)) {
				return true;
			}
		}
		return false;
	}
	
	public static JavaBasicTypeProxy fromValue(String basicType) {
		JavaBasicTypeProxy[] javaBasicTypes = JavaBasicTypeProxy.values();
		for (JavaBasicTypeProxy javaBasicType : javaBasicTypes) {
			if (javaBasicType.getBasicType().equals(basicType)) {
				return javaBasicType;
			}
		}
		return null;
	}
}
