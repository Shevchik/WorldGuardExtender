package WGExtender;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.sk89q.worldedit.bukkit.BukkitUtil;

public class LiquidFlow implements Listener {

	private Main main;
	private Config config;

	
	public LiquidFlow(Main main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onLiquidFlow(BlockFromToEvent e)
	{
		Block b = e.getBlock();
		if (b.isLiquid())
		{
			if (b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA)
			{
				if (config.blocklavaflow)
				{
					if (!allowLiquidFlow(b,e.getToBlock())) {e.setCancelled(true);}
				}
			} else
			if (b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER)
			{
				if (config.blockwaterflow)
				{
					if (!allowLiquidFlow(b,e.getToBlock())) {e.setCancelled(true);}
				}
			}
		}
	}
	
	
	private boolean allowLiquidFlow(Block from, Block to)
	{
		if (isInWGRegion(to))
		{
			//block flow from unclaimed area
			if (!isInWGRegion(from))
			{
				return false;
			}
			else
			//block flow from not the same regions
			{
				if (!isInTheSameRegion(from,to))
				{
					return false;
				}
			}
		}
		return true;
	}
	

	private boolean isInWGRegion(Block b)
	{
		if (main.wg.getRegionManager(b.getWorld()).getApplicableRegions(b.getLocation()).size() > 0) {return true;}
		return false;
	}
	
	private boolean isInTheSameRegion(Block b1, Block b2)
	{
		//plain equals doesn't want to work here :(
		List<String> ari1 = main.wg.getRegionManager(b1.getWorld()).getApplicableRegionsIDs(BukkitUtil.toVector(b1.getLocation()));
		List<String> ari2 = main.wg.getRegionManager(b2.getWorld()).getApplicableRegionsIDs(BukkitUtil.toVector(b2.getLocation()));
		if (ari1.equals(ari2))
		{
			return true;
		}
		return false;
	}
	
}
