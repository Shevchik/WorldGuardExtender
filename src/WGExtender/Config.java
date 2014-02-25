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
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;

public class Config {
	
	public boolean expandvert = false;
	
	public boolean blocklimitsenabled = false;
	public Map<String, Integer> blocklimits = new HashMap<String, Integer>();

	public boolean blocklavaflow = false;
	public boolean blockwaterflow = false;
	public boolean blockigniteotherregionbyplayer = false;
	public boolean blockfirespreadtoregion = false;
	public boolean blockfirespreadinregion = false;
	public boolean blockblockburninregion = false;
	public boolean blockentityexplosionblockdamage = false;
	public boolean blockentitydamagebyexplosion = false;
	public boolean blockpistonmoveblock = false;
	
	public boolean autoflagsenabled = false;
	public Map<Flag<?>, String> autoflags = new HashMap<Flag<?>,String>();
	
	public boolean restrictcommandsinregionsenabled = false;
	public Set<String> restrictedcommands = new HashSet<String>();
	
	public void loadConfig()
	{
		loadcfg();
		savecfg();
	}
	
	private void loadcfg()
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/WGExtender/config.yml"));
		
		expandvert = config.getBoolean("claim.vertexpand.enabled",expandvert);
		
		blocklimitsenabled = config.getBoolean("claim.blocklimits.enabled",blocklimitsenabled);
		blocklimits.clear();
		ConfigurationSection blimitscs = config.getConfigurationSection("claim.blocklimits.limits");
		if (blimitscs != null)
		{
			for (String group : blimitscs.getKeys(false))
			{
				blocklimits.put(group.toLowerCase(), blimitscs.getInt(group));
			}
		}

		blocklavaflow = config.getBoolean("regionprotect.flow.lava.enabled",blocklavaflow);
		blockwaterflow = config.getBoolean("regionprotect.flow.water.enabled",blockwaterflow);
		blockigniteotherregionbyplayer = config.getBoolean("regionprotect.ignitebyplayer.enabled",blockigniteotherregionbyplayer);
		blockfirespreadtoregion = config.getBoolean("regionprotect.fire.spread.toregion.enabled",blockfirespreadtoregion);
		blockfirespreadinregion = config.getBoolean("regionprotect.fire.spread.inregion.enabled",blockfirespreadinregion);
		blockblockburninregion = config.getBoolean("regionprotect.fire.burn.enabled",blockblockburninregion);
		blockentityexplosionblockdamage = config.getBoolean("regionprotect.explosion.block.enabled",blockentityexplosionblockdamage);
		blockentitydamagebyexplosion = config.getBoolean("regionprotect.explosion.entity.enabled",blockentitydamagebyexplosion);
		blockpistonmoveblock = config.getBoolean("regionprotect.pistonmove.enabled",blockpistonmoveblock);
		
		autoflagsenabled = config.getBoolean("autoflags.enabled",autoflagsenabled);
		autoflags.clear();
		ConfigurationSection aflagscs = config.getConfigurationSection("autoflags.flags");
		if (aflagscs != null)
		{
			for (String sflag : aflagscs.getKeys(false))
			{
				for (Flag<?> flag : DefaultFlag.getFlags())
				{
					if (flag.getName().equalsIgnoreCase(sflag))
					{
						autoflags.put(flag, aflagscs.getString(sflag));
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
		
		config.set("claim.vertexpand.enabled",expandvert);
		
		config.set("claim.blocklimits.enabled",blocklimitsenabled);
		if (blocklimits.isEmpty())
		{
			config.createSection("claim.blocklimits.limits");
		}
		else
		{
			for (String group : blocklimits.keySet())
			{
				config.set("claim.blocklimits.limits."+group, blocklimits.get(group));
			}
		}

		config.set("regionprotect.flow.lava.enabled",blocklavaflow);
		config.set("regionprotect.flow.water.enabled",blockwaterflow);
		config.set("regionprotect.ignitebyplayer.enabled",blockigniteotherregionbyplayer);
		config.set("regionprotect.fire.spread.toregion.enabled",blockfirespreadtoregion);
		config.set("regionprotect.fire.spread.inregion.enabled",blockfirespreadinregion);
		config.set("regionprotect.fire.burn.enabled",blockblockburninregion);
		config.set("regionprotect.explosion.block.enabled",blockentityexplosionblockdamage);
		config.set("regionprotect.explosion.entity.enabled",blockentitydamagebyexplosion);
		config.set("regionprotect.pistonmove.enabled",blockpistonmoveblock);
		
		config.set("autoflags.enabled",autoflagsenabled);
		if (autoflags.isEmpty())
		{
			config.createSection("autoflags.flags");
		} else
		{
			for (Flag<?> flag : autoflags.keySet())
			{
				config.set("autoflags.flags."+flag.getName(), autoflags.get(flag));
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
