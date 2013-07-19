package WGExtender;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Config config;
	private CommandProcess cmdprocess;
	
	@Override
	public void onEnable()
	{
		config = new Config();
		cmdprocess = new CommandProcess(config);
		getServer().getPluginManager().registerEvents(cmdprocess, this);
	}
	
	@Override
	public void onDisable()
	{
		config = null;
		cmdprocess = null;
	}
	
}
