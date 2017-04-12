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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import wgextender.commands.Commands;
import wgextender.features.claimcommand.WGRegionCommandWrapper;
import wgextender.features.custom.OldPVPFlagsHandler;
import wgextender.features.extendedwand.WEWandCommandWrapper;
import wgextender.features.extendedwand.WEWandListener;
import wgextender.features.flags.ChorusFruitUseFlag;
import wgextender.features.flags.FlagRegistration;
import wgextender.features.flags.OldPVPAttackSpeedFlag;
import wgextender.features.flags.OldPVPNoBowFlag;
import wgextender.features.flags.OldPVPNoShieldBlockFlag;
import wgextender.features.regionprotect.ownormembased.ChorusFruitFlagHandler;
import wgextender.features.regionprotect.ownormembased.IgniteByPlayer;
import wgextender.features.regionprotect.ownormembased.PvPHandlingListener;
import wgextender.features.regionprotect.ownormembased.RestrictCommands;
import wgextender.features.regionprotect.regionbased.BlockBurn;
import wgextender.features.regionprotect.regionbased.BlockExplode;
import wgextender.features.regionprotect.regionbased.EntityExplode;
import wgextender.features.regionprotect.regionbased.FireSpread;
import wgextender.features.regionprotect.regionbased.LiquidFlow;
import wgextender.features.regionprotect.regionbased.Pistons;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

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

	private PvPHandlingListener pvplistener;
	private OldPVPFlagsHandler oldpvphandler;

	@Override
	public void onEnable() {
		instance = this;
		log = getLogger();
		we = JavaPlugin.getPlugin(WorldEditPlugin.class);
		wg = JavaPlugin.getPlugin(WorldGuardPlugin.class);
		ChorusFruitUseFlag.assignInstance();
		OldPVPAttackSpeedFlag.assignInstance();
		OldPVPNoShieldBlockFlag.assignInstance();
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
		getServer().getPluginManager().registerEvents(new BlockExplode(config), this);
		getServer().getPluginManager().registerEvents(new WEWandListener(), this);
		getServer().getPluginManager().registerEvents(new ChorusFruitFlagHandler(), this);
		try {
			WGRegionCommandWrapper.inject(config);
			WEWandCommandWrapper.inject(config);
			FlagRegistration.registerFlag(ChorusFruitUseFlag.getInstance());
			FlagRegistration.registerFlag(OldPVPAttackSpeedFlag.getInstance());
			FlagRegistration.registerFlag(OldPVPNoShieldBlockFlag.getInstance());
			FlagRegistration.registerFlag(OldPVPNoBowFlag.getInstance());
			pvplistener = new PvPHandlingListener(config);
			pvplistener.inject();
			oldpvphandler = new OldPVPFlagsHandler();
			oldpvphandler.start();
		} catch (Throwable t) {
			log(Level.SEVERE, "Unable to inject, shutting down");
			t.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	public void onDisable() {
		try {
			WEWandCommandWrapper.uninject();
			WGRegionCommandWrapper.uninject();
			pvplistener.uninject();
			oldpvphandler.stop();
		} catch (Throwable t) {
			log(Level.SEVERE, "Unable to uninject, shutting down");
			t.printStackTrace();
			Bukkit.shutdown();
		}
		we = null;
		wg = null;
		instance = null;
	}

	public static void log(Level level, String message) {
		if (log != null) {
			log.log(Level.SEVERE, message);
		}
	}

}
