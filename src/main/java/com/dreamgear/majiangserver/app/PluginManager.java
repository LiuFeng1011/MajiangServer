package com.dreamgear.majiangserver.app;

import java.util.ArrayList;
import java.util.List;

public class PluginManager {
	private List<Plugin> plugins = new ArrayList<Plugin>();

	public void registPlugin(Plugin plugin)
	{
	  this.plugins.add(plugin);
	}

	public void unregistPlugin(Plugin plugin)
	{
	  this.plugins.remove(plugin);
	}

	public void notifyAppStart() throws Exception
	{
		for (Plugin plugin : this.plugins)
		{
			plugin.onAppStart();
		}
	}

	public void notifyAppStop() throws Exception
	{
		for (Plugin plugin : this.plugins)
		{
			plugin.onAppStop();
		}
	}


}
