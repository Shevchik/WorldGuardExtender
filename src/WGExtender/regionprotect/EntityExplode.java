package WGExtender.regionprotect;

import java.util.Iterator;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import WGExtender.Config;
import WGExtender.Main;

public class EntityExplode implements Listener {

	private Main main;
	private Config config;

	
	public EntityExplode(Main main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onEntityExplode(EntityExplodeEvent e)
	{
		if (!config.blockentityexplosionblockdamage) {return;}
		if (e.getEntity() instanceof TNTPrimed)
		{
			if (config.blocktntexplosionblockdmage)
			{
				filterProtectedBlocks(e.blockList());
			}
		} else
		if (e.getEntity() instanceof Creeper)
		{
			if (config.blockcreeperexplosionblockdmage)
			{
				filterProtectedBlocks(e.blockList());
			}
		}
		
	}
	
	
	private void filterProtectedBlocks(List<Block> blocklist)
	{
		Iterator<Block> it = blocklist.iterator();
		while (it.hasNext())
		{
			if (WGRPUtils.isInWGRegion(main.wg, it.next()))
			{
				it.remove();
			}
		}
	}
	
}
