package org.eclipse.plugin.openbox.apiunit.core.runner;

public class MethodArgument {
	private int index;
	private Class<?> clzz;
	private Object value;

	public MethodArgument(int index ,Class<?> clzz, Object value) {
		this.index = index;
		this.clzz = clzz;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Class<?> getClzz() {
		return clzz;
	}

	public Object getValue() {
//		String className = clzz.getName();
//		JavaBasicTypeProxy proxy = JavaBasicTypeProxy.fromValue(className);
//		if(null != proxy){
//			switch(proxy)
//			{
//			case BOOLEAN:
//				return Boolean.parseBoolean(value.toString());
//			case BYTE:
//				return Byte.parseByte(value.toString());
//			case CHAR:
//				return value;
//				//TODO
//			case DOUBLE:
//				return Double.parseDouble(value.toString());
//			case FLOAT:
//				return Float.parseFloat(value.toString());
//			case INT:
//				return Integer.parseInt(value.toString());
//			case LONG:
//				return Long.parseLong(value.toString());
//			case SHORT:
//				return Short.parseShort(value.toString());				
//			}
//		}
		return value;
	}
}
