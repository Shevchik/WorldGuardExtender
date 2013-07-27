package WGExtender;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Config config;
	private WGCommandProcess cmdprocess;
	private Commands commands;
	
	private Logger log = Bukkit.getLogger();
	
	@Override
	public void onEnable()
	{
		config = new Config();
		config.loadConfig();
		cmdprocess = new WGCommandProcess(this, config);
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
	
	
	public void debug(Object msg)
	{
		if (config.debug)
		{
			log.info(msg.toString());
		}
	}
	
}
