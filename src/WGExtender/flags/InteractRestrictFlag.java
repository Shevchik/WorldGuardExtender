package WGExtender.flags;

import com.sk89q.worldguard.protection.flags.StateFlag;

public class InteractRestrictFlag extends StateFlag {

	public static InteractRestrictFlag instance = null;

	public static void injectFlag() {
		instance = new InteractRestrictFlag();
		FlagInjector.injectFlag(instance);
    }

	public InteractRestrictFlag() {
		super("interact", true);
	}
	
}
