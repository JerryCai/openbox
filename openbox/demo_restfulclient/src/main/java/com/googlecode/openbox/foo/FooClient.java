package com.googlecode.openbox.foo;

import com.googlecode.openbox.foo.request.addfoo.AddFooParam;
import com.googlecode.openbox.foo.request.addfoo.AddFooResponse;
import com.googlecode.openbox.foo.request.deletefoo.DeleteFooResponse;
import com.googlecode.openbox.foo.request.getfoo.GetFooParam;
import com.googlecode.openbox.foo.request.getfoo.GetFooResponse;
import com.googlecode.openbox.http.responses.JsonResponse;

public interface FooClient extends FooClientExtention {

	JsonResponse<AddFooResponse> addFoo( AddFooParam params);

	JsonResponse<GetFooResponse> getFoo(GetFooParam params);
	
	JsonResponse<DeleteFooResponse> deleteFoo(String meetingId);

}