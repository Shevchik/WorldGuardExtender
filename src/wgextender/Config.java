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

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;

public class Config {

	protected final File configfile;
	public Config(WGExtender plugin) {
		configfile = new File(plugin.getDataFolder(), "config.yml");
	}

	public boolean expandvert = false;

	public boolean blocklimitsenabled = false;
	public Map<String, Integer> blocklimits = new LinkedHashMap<>();

	public boolean blocklavaflow = false;
	public boolean blockwaterflow = false;
	public boolean blockotherliquidflow = false;
	public boolean blockfirespreadtoregion = false;
	public boolean blockfirespreadinregion = false;
	public boolean blockblockburninregion = false;
	public boolean blockentityexplosionblockdamage = false;
	public boolean blockentitydamagebyexplosion = false;

	public boolean autoflagsenabled = false;
	public Map<Flag<?>, String> autoflags = new HashMap<>();

	public boolean restrictcommandsinregionsenabled = false;
	public Set<String> restrictedcommands = new HashSet<>();

	public boolean extendedwewand = false;

	public Boolean miscPvpMode = null;

	protected static final String miscPvpModeAllow = "allow";
	protected static final String miscPvpModeDeny = "deny";
	protected static final String miscPvpModeDefault = "default";

	public void loadConfig() {
		loadcfg();
		savecfg();
	}

	private void loadcfg() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		expandvert = config.getBoolean("claim.vertexpand", expandvert);

		blocklimitsenabled = config.getBoolean("claim.blocklimits.enabled", blocklimitsenabled);
		blocklimits.clear();
		ConfigurationSection blimitscs = config.getConfigurationSection("claim.blocklimits.limits");
		if (blimitscs != null) {
			for (String group : blimitscs.getKeys(false)) {
				blocklimits.put(group.toLowerCase(), blimitscs.getInt(group));
			}
		}

		blocklavaflow = config.getBoolean("regionprotect.flow.lava", blocklavaflow);
		blockwaterflow = config.getBoolean("regionprotect.flow.water", blockwaterflow);
		blockotherliquidflow = config.getBoolean("regionprotect.flow.other", blockotherliquidflow);
		blockfirespreadtoregion = config.getBoolean("regionprotect.fire.spread.toregion", blockfirespreadtoregion);
		blockfirespreadinregion = config.getBoolean("regionprotect.fire.spread.inregion", blockfirespreadinregion);
		blockblockburninregion = config.getBoolean("regionprotect.fire.burn", blockblockburninregion);
		blockentityexplosionblockdamage = config.getBoolean("regionprotect.explosion.block", blockentityexplosionblockdamage);
		blockentitydamagebyexplosion = config.getBoolean("regionprotect.explosion.entity", blockentitydamagebyexplosion);

		autoflagsenabled = config.getBoolean("autoflags.enabled",autoflagsenabled);
		autoflags.clear();
		ConfigurationSection aflagscs = config.getConfigurationSection("autoflags.flags");
		if (aflagscs != null) {
			for (String sflag : aflagscs.getKeys(false)) {
				Flag<?> flag = Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), sflag);
				if (flag != null) {
					autoflags.put(flag, aflagscs.getString(sflag));
				}
			}
		}

		restrictcommandsinregionsenabled = config.getBoolean("restrictcommands.enabled", restrictcommandsinregionsenabled);
		restrictedcommands = new HashSet<>(config.getStringList("restrictcommands.commands"));

		extendedwewand = config.getBoolean("extendedwewand", extendedwewand);

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

		config.set("claim.vertexpand", expandvert);

		config.set("claim.blocklimits.enabled", blocklimitsenabled);
		if (blocklimits.isEmpty()) {
			config.createSection("claim.blocklimits.limits");
		}
		for (Entry<String, Integer> entry : blocklimits.entrySet()) {
			config.set("claim.blocklimits.limits." + entry.getKey(), entry.getValue());
		}

		config.set("regionprotect.flow.lava", blocklavaflow);
		config.set("regionprotect.flow.water", blockwaterflow);
		config.set("regionprotect.flow.other", blockotherliquidflow);
		config.set("regionprotect.fire.spread.toregion", blockfirespreadtoregion);
		config.set("regionprotect.fire.spread.inregion", blockfirespreadinregion);
		config.set("regionprotect.fire.burn", blockblockburninregion);
		config.set("regionprotect.explosion.block", blockentityexplosionblockdamage);
		config.set("regionprotect.explosion.entity", blockentitydamagebyexplosion);

		config.set("autoflags.enabled", autoflagsenabled);
		if (autoflags.isEmpty()) {
			config.createSection("autoflags.flags");
		}
		for (Entry<Flag<?>, String> entry : autoflags.entrySet()) {
			config.set("autoflags.flags." + entry.getKey().getName(), entry.getValue());
		}

		config.set("restrictcommands.enabled", restrictcommandsinregionsenabled);
		config.set("restrictcommands.commands", new ArrayList<String>(restrictedcommands));

		config.set("extendedwewand", extendedwewand);

		config.set("misc.pvpmode", miscPvpMode != null ? miscPvpMode ? miscPvpModeAllow : miscPvpModeDeny : miscPvpModeDefault);

		try {config.save(configfile);} catch (IOException e) {}
	}

}
