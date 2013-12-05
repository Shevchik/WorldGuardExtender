package WGExtender.regionprotect.flagbased;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import WGExtender.WGExtender;
import WGExtender.flags.AnimalProtectFlag;
import WGExtender.utils.WGRegionUtils;

public class PlayerInteractBlocks implements Listener {

	private WGExtender main;
	public PlayerInteractBlocks(WGExtender main) {
		this.main = main;
	}

	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onEntityDamage(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		if (!WGRegionUtils.isFlagAllows(main.wg, player, block.getLocation(), AnimalProtectFlag.instance))
		{
			e.setCancelled(true);
		}
	}
	
}
