/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */

package WGExtender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

public class Config {
	
	public boolean expandvert = false;
	
	public boolean blocklimitsenabled = false;
	public HashMap<String, Integer> blocklimits = new HashMap<String, Integer>();
	
	public boolean blockliquidflow = false;
	public boolean blocklavaflow = false;
	public boolean blockwaterflow = false;
	public boolean blockigniteotherregionbyplayer = false;
	public boolean blockfirespreadtoregion = false;
	public boolean blockfirespreadinregion = false;
	public boolean blockblockburninregion = false;
	public boolean blockentityexplosionblockdamage = false;
	public boolean blockentitydamagebyexplosion = false;
	
	public boolean autoflagsenabled = false;
	@SuppressWarnings("rawtypes")
	public HashMap<Flag,State> autoflags = new HashMap<Flag,State>();
	
	public boolean restrictcommandsinregionsenabled = false;
	public HashSet<String> restrictedcommands = new HashSet<String>();
	
	public void loadConfig()
	{
		loadcfg();
		savecfg();
	}
	
	private void loadcfg()
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/WGExtender/config.yml"));
		
		expandvert = config.getBoolean("vertexpand.enabled",expandvert);
		
		blocklimitsenabled = config.getBoolean("blocklimits.enabled",blocklimitsenabled);
		blocklimits.clear();
		ConfigurationSection blimitscs = config.getConfigurationSection("blocklimits.limits");
		if (blimitscs != null)
		{
			for (String group : blimitscs.getKeys(false))
			{
				blocklimits.put(group, blimitscs.getInt(group));
			}
		}
		
		blockliquidflow = config.getBoolean("blockflowtoregion.enabled",blockliquidflow);
		blocklavaflow = config.getBoolean("blockflowtoregion.lava",blocklavaflow);
		blockwaterflow = config.getBoolean("blockflowtoregion.water",blockwaterflow);
		blockigniteotherregionbyplayer = config.getBoolean("blockigniteotherregionbyplayer.enabled",blockigniteotherregionbyplayer);
		blockfirespreadtoregion = config.getBoolean("blockfirespreadtoregion.enabled",blockfirespreadtoregion);
		blockfirespreadinregion = config.getBoolean("blockfirespreadinregion.enabled",blockfirespreadinregion);
		blockblockburninregion = config.getBoolean("blockblockburninregion.enabled",blockblockburninregion);
		blockentityexplosionblockdamage = config.getBoolean("blockentityexplosionblokdamage.enabled",blockentityexplosionblockdamage);
		blockentitydamagebyexplosion = config.getBoolean("blockentitydamagebyexplosion.enabled",blockentitydamagebyexplosion);
		
		autoflagsenabled = config.getBoolean("autoflags.enabled",autoflagsenabled);
		autoflags.clear();
		ConfigurationSection aflagscs = config.getConfigurationSection("autoflags.flags");
		if (aflagscs != null)
		{
			@SuppressWarnings("rawtypes")
			Flag[] flags = DefaultFlag.getFlags(); 
			for (String sflag : aflagscs.getKeys(false))
			{
				for (@SuppressWarnings("rawtypes") Flag flag : flags)
				{
					if (flag.getName().equalsIgnoreCase(sflag))
					{
						try {
							State state = State.valueOf(aflagscs.getString(sflag).toUpperCase());
							autoflags.put(flag, state);
						} catch (Exception e) {}
					}
				}
			}
		}
		
		restrictcommandsinregionsenabled = config.getBoolean("restrictcommands.enabled",restrictcommandsinregionsenabled);
		restrictedcommands = new HashSet<String>(config.getStringList("restrictcommands.commands"));
	}
	
	private void savecfg()
	{
		FileConfiguration config = new YamlConfiguration();
		
		config.set("vertexpand.enabled",expandvert);
		
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
		
		config.set("blockflowtoregion.enabled",blockliquidflow);
		config.set("blockflowtoregion.lava",blocklavaflow);
		config.set("blockflowtoregion.water",blockwaterflow);
		config.set("blockigniteotherregionbyplayer.enabled",blockigniteotherregionbyplayer);
		config.set("blockfirespreadtoregion.enabled",blockfirespreadtoregion);
		config.set("blockfirespreadinregion.enabled",blockfirespreadinregion);
		config.set("blockblockburninregion.enabled",blockblockburninregion);
		config.set("blockentityexplosionblokdamage.enabled",blockentityexplosionblockdamage);
		config.set("blockentitydamagebyexplosion.enabled",blockentitydamagebyexplosion);
		
		config.set("autoflags.enabled",autoflagsenabled);
		if (autoflags.isEmpty())
		{
			config.createSection("autoflags.flags");
		} else
		{
			for (@SuppressWarnings("rawtypes") Flag flag : autoflags.keySet())
			{
				config.set("autoflags.flags."+flag.getName(), autoflags.get(flag).toString());
			}
		}
		
		config.set("restrictcommands.enabled",restrictcommandsinregionsenabled);
		config.set("restrictcommands.commands",new ArrayList<String>(restrictedcommands));
		
		try {
			config.save(new File("plugins/WGExtender/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
