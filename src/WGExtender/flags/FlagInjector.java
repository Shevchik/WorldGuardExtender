package WGExtender.flags;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import WGExtender.WGExtender;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;

public class FlagInjector {

	protected static void injectFlag(Flag<?> flagtoinject) {
        try {
            Field field = DefaultFlag.class.getDeclaredField("flagsList");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.setAccessible(true);

            List<Flag<?>> flags = new ArrayList<Flag<?>>(Arrays.asList(DefaultFlag.getFlags()));
            flags.add(flagtoinject);
            field.set(null, flags.toArray(new Flag[flags.size()]));
            
            WorldGuardPlugin.class.cast(Bukkit.getPluginManager().getPlugin("WorldGuard")).getGlobalRegionManager().preload();
        } catch (Exception e) {
        	WGExtender.log(Level.SEVERE,"Failed to inject flag "+flagtoinject.getName());
            e.printStackTrace();
        }
    }
	
}
