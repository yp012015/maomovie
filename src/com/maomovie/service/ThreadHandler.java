package com.maomovie.service;

import org.json.JSONException;

public interface ThreadHandler {

	public Object run();

	public void result(Object result) throws JSONException;

}
