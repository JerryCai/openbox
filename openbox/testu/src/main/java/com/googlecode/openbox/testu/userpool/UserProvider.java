package com.googlecode.openbox.testu.userpool;

import java.util.List;

public interface UserProvider<T> {
	
	List<T> getUser(DC dc , int userNum , int fromIndex);
		
	int getInitUserNum();
	
	Class<T> getUserClass();
	
}
