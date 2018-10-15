package wgextender.features.flags;

import java.util.Map;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.flags.registry.UnknownFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import wgextender.utils.ReflectionUtils;
import wgextender.utils.WGRegionUtils;

public class FlagRegistration {

	@SuppressWarnings("unchecked")
	public static void registerFlag(Flag<?> flag) throws IllegalAccessException {
		//manually insert flag into the registry
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		Map<String, Flag<?>> flagMap = (Map<String, Flag<?>>) ReflectionUtils.getField(registry.getClass(), "flags").get(registry);
		Flag<?> prevFlag = flagMap.put(flag.getName().toLowerCase(), flag);
		//change flag instance in every loaded region if had old one
		if (prevFlag != null) {
			for (RegionManager rm : WGRegionUtils.getRegionContainer().getLoaded()) {
				for (ProtectedRegion region : rm.getRegions().values()) {
					Map<Flag<?>, Object> flags = region.getFlags();
					Object prevValue = flags.remove(prevFlag);
					if (prevValue != null) {
						//unknown flag will store marshaled value as value directly, so we can try to unmarshal it
						if (prevFlag instanceof UnknownFlag) {
							try {
								Object unmarshalled = flag.unmarshal(prevValue);
								if (unmarshalled != null) {
									flags.put(flag, unmarshalled);
								}
							} catch (Throwable t) {
							}
						}
						//before reload instance probably, try to marshal value first to see if it is compatible
						else {
							try {
								((Flag<Object>) flag).marshal(prevValue);
								flags.put(flag, prevValue);
							} catch (Throwable t) {
							}
						}
						region.setDirty(true);
					}
				}
			}
		}
	}

}
