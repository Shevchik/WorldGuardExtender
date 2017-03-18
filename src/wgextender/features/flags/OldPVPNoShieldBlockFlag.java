package wgextender.features.flags;

import com.sk89q.worldguard.protection.flags.BooleanFlag;

public class OldPVPNoShieldBlockFlag extends BooleanFlag {

	private static OldPVPNoShieldBlockFlag instance;

	public static OldPVPNoShieldBlockFlag getInstance() {
		return instance;
	}

	public static void assignInstance() {
		instance = new OldPVPNoShieldBlockFlag();
	}

	public OldPVPNoShieldBlockFlag() {
		super("oldpvp-noshieldblock");
	}

}
