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

	public boolean claimExpandSelectionVertical = false;

	public boolean claimBlockLimitsEnabled = false;
	public Map<String, Integer> claimBlockLimins = new LinkedHashMap<>();

	public boolean checkLavaFlow = false;
	public boolean checkWaterFlow = false;
	public boolean checkOtherLiquidFlow = false;
	public boolean checkFireSpreadToRegion = false;
	public boolean disableFireSpreadInRegion = false;
	public boolean disableBlockBurnInRegion = false;
	public boolean checkExplosionBlockDamage = false;
	public boolean checkExplosionEntityDamage = false;

	public boolean claimAutoFlagsEnabled = false;
	public Map<Flag<?>, String> claimAutoFlags = new HashMap<>();

	public boolean restrictCommandsInRegionEnabled = false;
	public Set<String> restrictedCommandsInRegion = new HashSet<>();

	public boolean extendedWorldEditWandEnabled = false;

	public Boolean miscDefaultPvPFlagOperationMode = null;

	protected static final String miscPvPFlagOperationModeAllow = "allow";
	protected static final String miscPvPFlagOperationModeDeny = "deny";
	protected static final String miscPvPFlagOperationModeDefault = "default";

	public void loadConfig() {
		loadcfg();
		savecfg();
	}

	private void loadcfg() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		claimExpandSelectionVertical = config.getBoolean("claim.vertexpand", claimExpandSelectionVertical);

		claimBlockLimitsEnabled = config.getBoolean("claim.blocklimits.enabled", claimBlockLimitsEnabled);
		claimBlockLimins.clear();
		ConfigurationSection blimitscs = config.getConfigurationSection("claim.blocklimits.limits");
		if (blimitscs != null) {
			for (String group : blimitscs.getKeys(false)) {
				claimBlockLimins.put(group.toLowerCase(), blimitscs.getInt(group));
			}
		}

		checkLavaFlow = config.getBoolean("regionprotect.flow.lava", checkLavaFlow);
		checkWaterFlow = config.getBoolean("regionprotect.flow.water", checkWaterFlow);
		checkOtherLiquidFlow = config.getBoolean("regionprotect.flow.other", checkOtherLiquidFlow);
		checkFireSpreadToRegion = config.getBoolean("regionprotect.fire.spread.toregion", checkFireSpreadToRegion);
		disableFireSpreadInRegion = config.getBoolean("regionprotect.fire.spread.inregion", disableFireSpreadInRegion);
		disableBlockBurnInRegion = config.getBoolean("regionprotect.fire.burn", disableBlockBurnInRegion);
		checkExplosionBlockDamage = config.getBoolean("regionprotect.explosion.block", checkExplosionBlockDamage);
		checkExplosionEntityDamage = config.getBoolean("regionprotect.explosion.entity", checkExplosionEntityDamage);

		claimAutoFlagsEnabled = config.getBoolean("autoflags.enabled",claimAutoFlagsEnabled);
		claimAutoFlags.clear();
		ConfigurationSection aflagscs = config.getConfigurationSection("autoflags.flags");
		if (aflagscs != null) {
			for (String sflag : aflagscs.getKeys(false)) {
				Flag<?> flag = Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), sflag);
				if (flag != null) {
					claimAutoFlags.put(flag, aflagscs.getString(sflag));
				}
			}
		}

		restrictCommandsInRegionEnabled = config.getBoolean("restrictcommands.enabled", restrictCommandsInRegionEnabled);
		restrictedCommandsInRegion = new HashSet<>(config.getStringList("restrictcommands.commands"));

		extendedWorldEditWandEnabled = config.getBoolean("extendedwewand", extendedWorldEditWandEnabled);

		String miscPvpModeStr = config.getString("misc.pvpmode", miscPvPFlagOperationModeDefault);
		if (miscPvpModeStr.equalsIgnoreCase(miscPvPFlagOperationModeAllow)) {
			miscDefaultPvPFlagOperationMode = Boolean.TRUE;
		} else if (miscPvpModeStr.equalsIgnoreCase(miscPvPFlagOperationModeDeny)) {
			miscDefaultPvPFlagOperationMode = Boolean.FALSE;
		} else {
			miscDefaultPvPFlagOperationMode = null;
		}
	}

	private void savecfg() {
		FileConfiguration config = new YamlConfiguration();

		config.set("claim.vertexpand", claimExpandSelectionVertical);

		config.set("claim.blocklimits.enabled", claimBlockLimitsEnabled);
		if (claimBlockLimins.isEmpty()) {
			config.createSection("claim.blocklimits.limits");
		}
		for (Entry<String, Integer> entry : claimBlockLimins.entrySet()) {
			config.set("claim.blocklimits.limits." + entry.getKey(), entry.getValue());
		}

		config.set("regionprotect.flow.lava", checkLavaFlow);
		config.set("regionprotect.flow.water", checkWaterFlow);
		config.set("regionprotect.flow.other", checkOtherLiquidFlow);
		config.set("regionprotect.fire.spread.toregion", checkFireSpreadToRegion);
		config.set("regionprotect.fire.spread.inregion", disableFireSpreadInRegion);
		config.set("regionprotect.fire.burn", disableBlockBurnInRegion);
		config.set("regionprotect.explosion.block", checkExplosionBlockDamage);
		config.set("regionprotect.explosion.entity", checkExplosionEntityDamage);

		config.set("autoflags.enabled", claimAutoFlagsEnabled);
		if (claimAutoFlags.isEmpty()) {
			config.createSection("autoflags.flags");
		}
		for (Entry<Flag<?>, String> entry : claimAutoFlags.entrySet()) {
			config.set("autoflags.flags." + entry.getKey().getName(), entry.getValue());
		}

		config.set("restrictcommands.enabled", restrictCommandsInRegionEnabled);
		config.set("restrictcommands.commands", new ArrayList<String>(restrictedCommandsInRegion));

		config.set("extendedwewand", extendedWorldEditWandEnabled);

		config.set("misc.pvpmode", miscDefaultPvPFlagOperationMode != null ? miscDefaultPvPFlagOperationMode ? miscPvPFlagOperationModeAllow : miscPvPFlagOperationModeDeny : miscPvPFlagOperationModeDefault);

		try {config.save(configfile);} catch (IOException e) {}
	}

}
