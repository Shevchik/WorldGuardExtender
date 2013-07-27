package WGExtender;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	public boolean debug = false;
	
	public boolean expandvert = true;
	
	public boolean blocklimitsenabled = true;
	public HashMap<String, Integer> blocklimits = new HashMap<String, Integer>();
	
	public void loadConfig()
	{
		loadcfg();
		savecfg();
	}
	
	private void loadcfg()
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/WGExtender/config.yml"));
		
		debug = config.getBoolean("debug.enabled",debug);
		
		blocklimitsenabled = config.getBoolean("blocklimits.enabled",blocklimitsenabled);
		blocklimits.clear();
		if (config.getConfigurationSection("blocklimits.limits") != null)
		{
			for (String group : config.getConfigurationSection("blocklimits.limits").getKeys(false))
			{
				blocklimits.put(group, config.getInt("blocklimits.limits."+group));
			}
		}
	}
	
	private void savecfg()
	{
		FileConfiguration config = new YamlConfiguration();
		
		config.set("debug.enabled",debug);
		
		config.set("blocklimits.enabled",blocklimitsenabled);
		if (blocklimits.isEmpty())
		{
			config.createSection("blocklimits.limits");
		}
		else
		{
			for (String group : blocklimits.keySet())
			{
				config.set("blocklimits.limits."+group, blocklimits.get(group));
			}
		}
		
		try {
			config.save(new File("plugins/WGExtender/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
