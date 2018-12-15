
package com.black.web.base.controllers;

import com.google.gson.Gson;

import com.black.web.Logger.Logger;

public class RaysController {
	
	public final Gson gson = new Gson();
	
	
    public String op(String path) {
    	Logger.debug("OP:" + path);
        return ok("");
    }
    
    public String ok(String path) {
        return path;
    }
}
