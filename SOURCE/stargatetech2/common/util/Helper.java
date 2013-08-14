package stargatetech2.common.util;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;

public class Helper {
	public static ForgeDirection yaw2dir(float yaw){
		int dir = (MathHelper.floor_double((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3)+3;
		if(dir > 4) dir -= 4;
		switch(dir){
			case 1: return ForgeDirection.SOUTH;
			case 2: return ForgeDirection.WEST;
			case 3: return ForgeDirection.NORTH;
			case 4: return ForgeDirection.EAST;
			default: return ForgeDirection.UNKNOWN;
		}
	}
}