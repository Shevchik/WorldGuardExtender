package WGExtender.regionprotect;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

public class WGRPUtils {


	protected static boolean isInWGRegion(WorldGuardPlugin wg, Block b)
	{
		if (wg.getRegionManager(b.getWorld()).getApplicableRegions(b.getLocation()).size() > 0) {return true;}
		return false;
	}
	
	protected static boolean isInTheSameRegion(WorldGuardPlugin wg, Block b1, Block b2)
	{
		//plain equals doesn't want to work here :(
		List<String> ari1 = wg.getRegionManager(b1.getWorld()).getApplicableRegionsIDs(BukkitUtil.toVector(b1.getLocation()));
		List<String> ari2 = wg.getRegionManager(b2.getWorld()).getApplicableRegionsIDs(BukkitUtil.toVector(b2.getLocation()));
		if (ari1.equals(ari2))
		{
			return true;
		}
		return false;
	}
	
	
	protected static boolean isOwnerOrMember(WorldGuardPlugin wg, Player p, Block b)
	{
		ApplicableRegionSet ars = wg.getRegionManager(b.getWorld()).getApplicableRegions(b.getLocation());
		if (ars.isOwnerOfAll(wg.wrapPlayer(p)) || ars.isMemberOfAll(wg.wrapPlayer(p))) 
		{
			return true;
		}
		return false;
	}

}
