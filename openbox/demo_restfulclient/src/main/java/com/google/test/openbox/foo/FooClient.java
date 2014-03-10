package com.google.test.openbox.foo;

import com.google.test.openbox.foo.request.addfoo.AddFooParam;
import com.google.test.openbox.foo.request.addfoo.AddFooResponse;
import com.google.test.openbox.foo.request.deletefoo.DeleteFooResponse;
import com.google.test.openbox.foo.request.getfoo.GetFooParam;
import com.google.test.openbox.foo.request.getfoo.GetFooResponse;
import com.google.test.openbox.http.responses.JsonResponse;

public interface FooClient extends FooClientExtention {

	JsonResponse<AddFooResponse> addFoo( AddFooParam params);

	JsonResponse<GetFooResponse> getFoo(GetFooParam params);
	
	JsonResponse<DeleteFooResponse> deleteFoo(String id);

}