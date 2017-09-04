package com.dreamgear.majiangserver.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerApp {
	private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);
	private static ServerApp instance;
	private PluginManager plugins;
	
	public static ServerApp getInstance()
	{
	    if (instance == null)
	    {
	      instance = new ServerApp();
	    }

	    return instance;
	}
	
	private ServerApp(){

	    try
	    {
	        this.plugins = new PluginManager();
	        

	        this.plugins.registPlugin(new Plugin()
	        {
				public void onAppStart() throws Exception {
					// TODO Auto-generated method stub
			        logger.info("****************App start**************");
				}

				public void onAppStop() throws Exception {
					// TODO Auto-generated method stub

			        logger.info("****************App stop**************");
				}
	        });
	        
	        //关闭回调
	        Runtime.getRuntime().addShutdownHook(new Thread()
	        {
	          public void run()
	          {
	            try
	            {
	            	ServerApp.getInstance().stop();
	            }
	            catch (Exception e)
	            {
	              e.printStackTrace();
	            }
	          }

	        });
	    }catch (Exception e){
	    	logger.error(e.toString());
	        System.exit(0);
	    }
	}

	public void start() throws Exception
	{
		this.plugins.notifyAppStart();
	}
	
	public void stop() throws Exception
	{
		this.plugins.notifyAppStop();
	}
	
	public void registPlugin(Plugin plugin) throws Exception
	{
		this.plugins.registPlugin(plugin);
	}

}
