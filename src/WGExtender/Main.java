package WGExtender;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Config config;
	private WGCommandProcess cmdprocess;
	
	@Override
	public void onEnable()
	{
		config = new Config();
		config.loadConfig();
		cmdprocess = new WGCommandProcess(config);
		getServer().getPluginManager().registerEvents(cmdprocess, this);
	}
	
	@Override
	public void onDisable()
	{
		config = null;
		cmdprocess = null;
	}
	
}
