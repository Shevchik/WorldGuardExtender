package WGExtender.regionprotect.flagbased;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import WGExtender.WGExtender;
import WGExtender.flags.AnimalProtectFlag;
import WGExtender.utils.WGRegionUtils;

public class AttackByPlayer implements Listener {
	
	private WGExtender main;
	public AttackByPlayer(WGExtender main) {
		this.main = main;
	}

	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onEntityDamage(EntityDamageByEntityEvent e)
	{
		Entity entity = e.getEntity();
		if (entity instanceof Animals && WGRegionUtils.isInWGRegion(main.wg, entity.getLocation()))
		{
			Player damagerplayer = null;
			Entity edamager = e.getDamager();
			if (edamager instanceof Player) 
			{
				damagerplayer = (Player) edamager;
			} else 
			if (edamager instanceof Arrow)
			{
				Arrow arrow = (Arrow) edamager;
				if (arrow.getShooter() instanceof Player)
				{
					damagerplayer = (Player) arrow.getShooter();
				}
			}
			if (damagerplayer != null)
			{
				if (!WGRegionUtils.isFlagAllows(main.wg, damagerplayer, entity, AnimalProtectFlag.instance))
				{
					if (!damagerplayer.hasPermission("worldguard.region.bypass."+damagerplayer.getWorld().getName()))
					{
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	

}
