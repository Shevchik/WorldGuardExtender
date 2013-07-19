package WGExtender;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	public HashMap<String, Integer> blocklimits = new HashMap<String, Integer>();
	
	public void loadConfig()
	{
		loadcfg();
		savecfg();
	}
	
	private void loadcfg()
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/WGExtender/config.yml"));
		for (String group : config.getConfigurationSection("blocklimits").getKeys(false))
		{
			blocklimits.put(group, config.getInt("blocklimits."+group));
		}
	}
	
	private void savecfg()
	{
		FileConfiguration config = new YamlConfiguration();
		if (blocklimits.isEmpty())
		{
			config.createSection("blocklimits");
		}
		else
		{
			for (String group : blocklimits.keySet())
			{
				config.set("blocklimits."+group, blocklimits.get(group));
			}
		}
		try {
			config.save(new File("plugins/WGExtender/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
