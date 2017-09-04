package com.dreamgear;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.http.HttpServer;
import com.dreamgear.majiang.game.GameWord;
import com.dreamgear.majiangserver.app.ServerApp;
import com.dreamgear.majiangserver.app.Plugin;
/**
 * Hello world!
 *
 */
public class MajiangApp 
{
	private static final Logger logger = LoggerFactory.getLogger(MajiangApp.class);
	
    public static void main( String[] args ) throws Exception
    {
    	final ServerApp app = ServerApp.getInstance();
    	System.out.println("=======main=============");
    	app.registPlugin(new Plugin()
        {
			public void onAppStart() throws Exception {
				// TODO Auto-generated method stub

		        logger.info("-----------service start------------");
			}

			public void onAppStop() throws Exception {
				// TODO Auto-generated method stub
				HttpServer.getInstance().stop();
		        logger.info("-----------service stop-----------");
			}
        });
    	
    	GameWord.GetInstance().StartGame();
    	app.start();

    }
}
