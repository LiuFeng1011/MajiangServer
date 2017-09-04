package com.dreamgear.majiangserver.app;

public abstract interface Plugin {
	  public abstract void onAppStart()
			    throws Exception;
	
	  public abstract void onAppStop()
			    throws Exception;
}
