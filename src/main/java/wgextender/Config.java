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

package wgextender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;

public class Config {

	private File configfile;
	public Config(WGExtender plugin) {
		configfile = new File(plugin.getDataFolder(), "config.yml");
	}

	public boolean expandvert = false;

	public boolean blocklimitsenabled = false;
	public LinkedHashMap<String, Integer> blocklimits = new LinkedHashMap<String, Integer>();

	public boolean blocklavaflow = false;
	public boolean blockwaterflow = false;
	public boolean blockotherliquidflow = false;
	public boolean blockigniteotherregionbyplayer = false;
	public boolean blockfirespreadtoregion = false;
	public boolean blockfirespreadinregion = false;
	public boolean blockblockburninregion = false;
	public boolean blockentityexplosionblockdamage = false;
	public boolean blockentitydamagebyexplosion = false;
	public boolean blockpistonmoveblock = false;

	public boolean autoflagsenabled = false;
	public Map<Flag<?>, String> autoflags = new HashMap<Flag<?>, String>();

	public boolean restrictcommandsinregionsenabled = false;
	public Set<String> restrictedcommands = new HashSet<String>();

	public boolean extendedwewand = false;

	public Boolean miscPvpMode = null;
	private final String miscPvpModeAllow = "allow";
	private final String miscPvpModeDeny = "deny";
	private final String miscPvpModeDefault = "default";

	public void loadConfig() {
		loadcfg();
		savecfg();
	}

	private void loadcfg() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		expandvert = config.getBoolean("claim.vertexpand.enabled", expandvert);

		blocklimitsenabled = config.getBoolean("claim.blocklimits.enabled", blocklimitsenabled);
		blocklimits.clear();
		ConfigurationSection blimitscs = config.getConfigurationSection("claim.blocklimits.limits");
		if (blimitscs != null) {
			for (String group : blimitscs.getKeys(false)) {
				blocklimits.put(group.toLowerCase(), blimitscs.getInt(group));
			}
		}

		blocklavaflow = config.getBoolean("regionprotect.flow.lava.enabled", blocklavaflow);
		blockwaterflow = config.getBoolean("regionprotect.flow.water.enabled", blockwaterflow);
		blockotherliquidflow = config.getBoolean("regionprotect.flow.otherliquid.enabled", blockotherliquidflow);
		blockigniteotherregionbyplayer = config.getBoolean("regionprotect.ignitebyplayer.enabled", blockigniteotherregionbyplayer);
		blockfirespreadtoregion = config.getBoolean("regionprotect.fire.spread.toregion.enabled", blockfirespreadtoregion);
		blockfirespreadinregion = config.getBoolean("regionprotect.fire.spread.inregion.enabled", blockfirespreadinregion);
		blockblockburninregion = config.getBoolean("regionprotect.fire.burn.enabled", blockblockburninregion);
		blockentityexplosionblockdamage = config.getBoolean("regionprotect.explosion.block.enabled", blockentityexplosionblockdamage);
		blockentitydamagebyexplosion = config.getBoolean("regionprotect.explosion.entity.enabled", blockentitydamagebyexplosion);
		blockpistonmoveblock = config.getBoolean("regionprotect.pistonmove.enabled", blockpistonmoveblock);

		autoflagsenabled = config.getBoolean("autoflags.enabled",autoflagsenabled);
		autoflags.clear();
		ConfigurationSection aflagscs = config.getConfigurationSection("autoflags.flags");
		if (aflagscs != null) {
			for (String sflag : aflagscs.getKeys(false)) {
				Flag<?> flag = DefaultFlag.fuzzyMatchFlag(WGExtender.getWorldGuard().getFlagRegistry(), sflag);
				if (flag != null) {
					autoflags.put(flag, aflagscs.getString(sflag));
				}
			}
		}

		restrictcommandsinregionsenabled = config.getBoolean("restrictcommands.enabled", restrictcommandsinregionsenabled);
		restrictedcommands = new HashSet<String>(config.getStringList("restrictcommands.commands"));

		extendedwewand = config.getBoolean("extendedwewand.enabled", extendedwewand);

		String miscPvpModeStr = config.getString("misc.pvpmode", miscPvpModeDefault);
		if (miscPvpModeStr.equalsIgnoreCase(miscPvpModeAllow)) {
			miscPvpMode = Boolean.TRUE;
		} else if (miscPvpModeStr.equalsIgnoreCase(miscPvpModeDeny)) {
			miscPvpMode = Boolean.FALSE;
		} else {
			miscPvpMode = null;
		}
	}

	private void savecfg() {
		FileConfiguration config = new YamlConfiguration();

		config.set("claim.vertexpand.enabled", expandvert);

		config.set("claim.blocklimits.enabled", blocklimitsenabled);
		if (blocklimits.isEmpty()) {
			config.createSection("claim.blocklimits.limits");
		}
		for (Entry<String, Integer> entry : blocklimits.entrySet()) {
			config.set("claim.blocklimits.limits." + entry.getKey(), entry.getValue());
		}

		config.set("regionprotect.flow.lava.enabled", blocklavaflow);
		config.set("regionprotect.flow.water.enabled", blockwaterflow);
		config.set("regionprotect.flow.otherliquid.enabled", blockotherliquidflow);
		config.set("regionprotect.ignitebyplayer.enabled", blockigniteotherregionbyplayer);
		config.set("regionprotect.fire.spread.toregion.enabled", blockfirespreadtoregion);
		config.set("regionprotect.fire.spread.inregion.enabled", blockfirespreadinregion);
		config.set("regionprotect.fire.burn.enabled", blockblockburninregion);
		config.set("regionprotect.explosion.block.enabled", blockentityexplosionblockdamage);
		config.set("regionprotect.explosion.entity.enabled", blockentitydamagebyexplosion);
		config.set("regionprotect.pistonmove.enabled", blockpistonmoveblock);

		config.set("autoflags.enabled", autoflagsenabled);
		if (autoflags.isEmpty()) {
			config.createSection("autoflags.flags");
		}
		for (Entry<Flag<?>, String> entry : autoflags.entrySet()) {
			config.set("autoflags.flags." + entry.getKey().getName(), entry.getValue());
		}

		config.set("restrictcommands.enabled", restrictcommandsinregionsenabled);
		config.set("restrictcommands.commands", new ArrayList<String>(restrictedcommands));

		config.set("extendedwewand.enabled", extendedwewand);

		config.set("misc.pvpmode", miscPvpMode != null ? miscPvpMode ? miscPvpModeAllow : miscPvpModeDeny : miscPvpModeDefault);

		try {config.save(configfile);} catch (IOException e) {}
	}

}
