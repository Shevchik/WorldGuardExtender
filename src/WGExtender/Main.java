package WGExtender;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Config config;
	private WGCommandProcess cmdprocess;
	private Commands commands;
	
	@Override
	public void onEnable()
	{
		config = new Config();
		config.loadConfig();
		cmdprocess = new WGCommandProcess(config);
		getServer().getPluginManager().registerEvents(cmdprocess, this);
		commands = new Commands(config);
		getCommand("wgex").setExecutor(commands);
	}
	
	@Override
	public void onDisable()
	{
		config = null;
		cmdprocess = null;
		commands = null;
		HandlerList.unregisterAll(this);
	}
	
}
