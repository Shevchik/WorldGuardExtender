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

	private File configFile;
	public Config(WGExtender plugin) {
		configFile = new File(plugin.getDataFolder(), "config.yml");
	}

	public boolean expandByVertical = false;

	public boolean blockLimitsEnabled = false;
	public LinkedHashMap<String, Integer> blockLimits = new LinkedHashMap<String, Integer>();

	public boolean blockLavaFlow = false;
	public boolean blockWaterFlow = false;
	public boolean blockOtherLiquidFlow = false;
	public boolean blockIgniteOtherRegionByPlayer = false;
	public boolean blockFireSpreadToRegion = false;
	public boolean blockFireSpreadInRegion = false;
	public boolean blockBlockBurnInRegion = false;
	public boolean blockEntityExplosionBlockDamage = false;
	public boolean blockEntityDamageByExplosion = false;
	public boolean blockPistonMoveBlock = false;

	public boolean autoFlagsEnabled = false;
	public Map<Flag<?>, String> autoFlags = new HashMap<Flag<?>, String>();

	public boolean restrictCommandsInRegionsEnabled = false;
	public Set<String> restrictedCommands = new HashSet<String>();

	public boolean extendedWEWand = false;

	public Boolean miscPvpMode = null;
	private final String miscPvpModeAllow = "allow";
	private final String miscPvpModeDeny = "deny";
	private final String miscPvpModeDefault = "default";

	public void loadConfig() {
		loadcfg();
		savecfg();
	}

	private void loadcfg() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

		expandByVertical = config.getBoolean("claim.vertexpand.enabled", expandByVertical);

		blockLimitsEnabled = config.getBoolean("claim.blockLimits.enabled", blockLimitsEnabled);
		blockLimits.clear();
		ConfigurationSection blimitscs = config.getConfigurationSection("claim.blocklimits.limits");
		if (blimitscs != null) {
			for (String group : blimitscs.getKeys(false)) {
				blockLimits.put(group.toLowerCase(), blimitscs.getInt(group));
			}
		}

		blockLavaFlow = config.getBoolean("regionprotect.flow.lava.enabled", blockLavaFlow);
		blockWaterFlow = config.getBoolean("regionprotect.flow.water.enabled", blockWaterFlow);
		blockOtherLiquidFlow = config.getBoolean("regionprotect.flow.otherliquid.enabled", blockOtherLiquidFlow);
		blockIgniteOtherRegionByPlayer = config.getBoolean("regionprotect.ignitebyplayer.enabled", blockIgniteOtherRegionByPlayer);
		blockFireSpreadToRegion = config.getBoolean("regionprotect.fire.spread.toregion.enabled", blockFireSpreadToRegion);
		blockFireSpreadInRegion = config.getBoolean("regionprotect.fire.spread.inregion.enabled", blockFireSpreadInRegion);
		blockBlockBurnInRegion = config.getBoolean("regionprotect.fire.burn.enabled", blockBlockBurnInRegion);
		blockEntityExplosionBlockDamage = config.getBoolean("regionprotect.explosion.block.enabled", blockEntityExplosionBlockDamage);
		blockEntityDamageByExplosion = config.getBoolean("regionprotect.explosion.entity.enabled", blockEntityDamageByExplosion);
		blockPistonMoveBlock = config.getBoolean("regionprotect.pistonmove.enabled", blockPistonMoveBlock);

		autoFlagsEnabled = config.getBoolean("autoflags.enabled", autoFlagsEnabled);
		autoFlags.clear();
		ConfigurationSection aflagscs = config.getConfigurationSection("autoflags.flags");
		if (aflagscs != null) {
			for (String sflag : aflagscs.getKeys(false)) {
				Flag<?> flag = DefaultFlag.fuzzyMatchFlag(WGExtender.getWorldGuard().getFlagRegistry(), sflag);
				if (flag != null) {
					autoFlags.put(flag, aflagscs.getString(sflag));
				}
			}
		}

		restrictCommandsInRegionsEnabled = config.getBoolean("restrictcommands.enabled", restrictCommandsInRegionsEnabled);
		restrictedCommands = new HashSet<String>(config.getStringList("restrictcommands.commands"));

		extendedWEWand = config.getBoolean("extendedwewand.enabled", extendedWEWand);

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

		config.set("claim.vertexpand.enabled", expandByVertical);

		config.set("claim.blocklimits.enabled", blockLimitsEnabled);
		if (blockLimits.isEmpty()) {
			config.createSection("claim.blocklimits.limits");
		}
		for (Entry<String, Integer> entry : blockLimits.entrySet()) {
			config.set("claim.blockLimits.limits." + entry.getKey(), entry.getValue());
		}

		config.set("regionprotect.flow.lava.enabled", blockLavaFlow);
		config.set("regionprotect.flow.water.enabled", blockWaterFlow);
		config.set("regionprotect.flow.otherliquid.enabled", blockOtherLiquidFlow);
		config.set("regionprotect.ignitebyplayer.enabled", blockIgniteOtherRegionByPlayer);
		config.set("regionprotect.fire.spread.toregion.enabled", blockFireSpreadToRegion);
		config.set("regionprotect.fire.spread.inregion.enabled", blockFireSpreadInRegion);
		config.set("regionprotect.fire.burn.enabled", blockBlockBurnInRegion);
		config.set("regionprotect.explosion.block.enabled", blockEntityExplosionBlockDamage);
		config.set("regionprotect.explosion.entity.enabled", blockEntityDamageByExplosion);
		config.set("regionprotect.pistonmove.enabled", blockPistonMoveBlock);

		config.set("autoflags.enabled", autoFlagsEnabled);
		if (autoFlags.isEmpty()) {
			config.createSection("autoflags.flags");
		}
		for (Entry<Flag<?>, String> entry : autoFlags.entrySet()) {
			config.set("autoflags.flags." + entry.getKey().getName(), entry.getValue());
		}

		config.set("restrictcommands.enabled", restrictCommandsInRegionsEnabled);
		config.set("restrictcommands.commands", new ArrayList<String>(restrictedCommands));

		config.set("extendedwewand.enabled", extendedWEWand);

		config.set("misc.pvpmode", miscPvpMode != null ? miscPvpMode ? miscPvpModeAllow : miscPvpModeDeny : miscPvpModeDefault);

		try {config.save(configFile);} catch (IOException e) {}
	}

}
