package wgextender.features.flags;

import com.sk89q.worldguard.protection.flags.BooleanFlag;

public class OldPVPAttackSpeedFlag extends BooleanFlag {

	private static OldPVPAttackSpeedFlag instance;

	public static OldPVPAttackSpeedFlag getInstance() {
		return instance;
	}

	public static void assignInstance() {
		instance = new OldPVPAttackSpeedFlag();
	}

	public OldPVPAttackSpeedFlag() {
		super("oldpvp-attackspeed");
	}

}
