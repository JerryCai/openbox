package com.google.test.openbox.testu.userpool;

import java.util.List;

public interface UserPool<T> {

	T borrowUser(DC dc);

	List<T> borrowUsers(DC dc, int num);

	void returnUser(DC dc, T user);

	void returnUsers(DC dc, List<T> users);

}
