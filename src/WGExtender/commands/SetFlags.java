package WGExtender.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class SetFlags {

	public static <V> void setFlags(WorldGuardPlugin wg, Flag<V> flag, String args, World world) throws InvalidFlagFormat, ProtectionDatabaseException 
	{
		RegionManager rm = wg.getRegionManager(world);
		for (ProtectedRegion region : rm.getRegions().values())
		{
			region.setFlag(flag, flag.parseInput(wg, Bukkit.getConsoleSender(), args));
		}
		rm.save();
	}

}
