package com.google.test.openbox.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonFactory {

	public static Gson createGson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

}
