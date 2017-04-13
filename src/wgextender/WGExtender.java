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

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import wgextender.commands.Commands;
import wgextender.features.claimcommand.WGRegionCommandWrapper;
import wgextender.features.custom.OldPVPFlagsHandler;
import wgextender.features.extendedwand.WEWandCommandWrapper;
import wgextender.features.extendedwand.WEWandListener;
import wgextender.features.flags.*;
import wgextender.features.regionprotect.ownormembased.ChorusFruitFlagHandler;
import wgextender.features.regionprotect.ownormembased.IgniteByPlayer;
import wgextender.features.regionprotect.ownormembased.PvPHandlingListener;
import wgextender.features.regionprotect.ownormembased.RestrictCommands;
import wgextender.features.regionprotect.regionbased.*;
import wgextender.utils.VersionUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WGExtender extends JavaPlugin {

	private static WGExtender instance;
	public static WGExtender getInstance() {
		return instance;
	}

	private static Logger log;

	private static WorldEditPlugin we = null;
	public static WorldEditPlugin getWorldEdit() {
		return we;
	}

	private static WorldGuardPlugin wg = null;
	public static WorldGuardPlugin getWorldGuard() {
		return wg;
	}

	private PvPHandlingListener pvpListener;
	private OldPVPFlagsHandler oldPvpHandler;

	@Override
	public void onEnable() {
		instance = this;
		log = getLogger();
		we = JavaPlugin.getPlugin(WorldEditPlugin.class);
		wg = JavaPlugin.getPlugin(WorldGuardPlugin.class);

		OldPVPNoBowFlag.assignInstance();
		Config config = new Config(this);
		config.loadConfig();
		getCommand("wgex").setExecutor(new Commands(config));
		getServer().getPluginManager().registerEvents(new RestrictCommands(config), this);
		getServer().getPluginManager().registerEvents(new LiquidFlow(config), this);
		getServer().getPluginManager().registerEvents(new IgniteByPlayer(config), this);
		getServer().getPluginManager().registerEvents(new FireSpread(config), this);
		getServer().getPluginManager().registerEvents(new BlockBurn(config), this);
		getServer().getPluginManager().registerEvents(new Pistons(config), this);
		getServer().getPluginManager().registerEvents(new EntityExplode(config), this);
		getServer().getPluginManager().registerEvents(new WEWandListener(), this);

		// Flags for new versions
		if (VersionUtils.isMC183OrNewer()) {
			getServer().getPluginManager().registerEvents(new BlockExplode(config), this);
		}

		if (VersionUtils.isMC19OrNewer()) {
			OldPVPAttackSpeedFlag.assignInstance();
			OldPVPNoShieldBlockFlag.assignInstance();
			ChorusFruitUseFlag.assignInstance();
			getServer().getPluginManager().registerEvents(new ChorusFruitFlagHandler(), this);
		}

		try {
			WGRegionCommandWrapper.inject(config);
			WEWandCommandWrapper.inject(config);

			if (VersionUtils.isMC19OrNewer()) {
				FlagRegistration.registerFlag(ChorusFruitUseFlag.getInstance());
				FlagRegistration.registerFlag(OldPVPAttackSpeedFlag.getInstance());
				FlagRegistration.registerFlag(OldPVPNoShieldBlockFlag.getInstance());
			}

			FlagRegistration.registerFlag(OldPVPNoBowFlag.getInstance());
			pvpListener = new PvPHandlingListener(config);
			pvpListener.inject();
			oldPvpHandler = new OldPVPFlagsHandler();
			oldPvpHandler.start();
		} catch (Throwable t) {
			severe("Unable to inject, shutting down");
			t.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public void onDisable() {
		try {
			WEWandCommandWrapper.uninject();
			WGRegionCommandWrapper.uninject();
			pvpListener.uninject();
			oldPvpHandler.stop();
		} catch (Throwable t) {
			severe("Unable to uninject, shutting down");
			t.printStackTrace();
			Bukkit.shutdown();
		}
		we = null;
		wg = null;
		instance = null;
	}

	public static void severe(String message) {
		if (log != null) {
			log.log(Level.SEVERE, message);
		}
	}

}
