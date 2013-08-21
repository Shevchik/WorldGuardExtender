package WGExtender;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin {

	private Logger log = Bukkit.getLogger();
	
	private Config config;
	private WGCommandProcess cmdprocess;
	private Commands commands;
	private LiquidFlow lflow;
	
	public WorldEditPlugin we = null;
	public WorldGuardPlugin wg = null;
	
	@Override
	public void onEnable()
	{
		config = new Config();
		config.loadConfig();
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		cmdprocess = new WGCommandProcess(this, config);
		getServer().getPluginManager().registerEvents(cmdprocess, this);
		commands = new Commands(config);
		getCommand("wgex").setExecutor(commands);
		lflow = new LiquidFlow(this,config);
		getServer().getPluginManager().registerEvents(lflow, this);
	}
	
	@Override
	public void onDisable()
	{
		config = null;
		cmdprocess = null;
		lflow = null;
		commands = null;
		HandlerList.unregisterAll(this);
		we = null;
		wg = null;
		log = null;
	}
	
	
	public void debug(Object msg)
	{
		if (config.debug)
		{
			log.info(msg.toString());
		}
	}
	
}
