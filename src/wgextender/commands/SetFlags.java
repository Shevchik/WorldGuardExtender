package wgextender.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;

import wgextender.WGExtender;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class SetFlags {

	public static <V> void setFlags(Flag<V> flag, String args, World world) throws Exception {
		RegionManager rm = WGExtender.getInstance().getWorldGuard().getRegionManager(world);
		for (ProtectedRegion region : rm.getRegions().values()) {
			if (region.getId().equals("__global__")) {
				continue;
			}
			region.setFlag(flag, flag.parseInput(WGExtender.getInstance().getWorldGuard(), Bukkit.getConsoleSender(), args));
		}
		rm.save();
	}

}
