package lordfokas.stargatetech2.util;

import java.io.File;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.DimensionManager;

public class Helper {
	public static EnumFacing yaw2dir(float yaw, float pitch, boolean vertical){
		if(vertical){
			if(pitch < -45) return EnumFacing.DOWN;
			if(pitch >  45) return EnumFacing.UP;
		}
		int dir = (MathHelper.floor_double((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3)+3;
		if(dir > 4) dir -= 4;
		switch(dir){
			case 1: return EnumFacing.SOUTH;
			case 2: return EnumFacing.WEST;
			case 3: return EnumFacing.NORTH;
			case 4: return EnumFacing.EAST;
			default: return null; // XXX: NPE incoming
		}
	}
	
	public static File getFile(String file){
		return new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + File.separator + "SGTech2_" + file);
	}
	
	public static String prettyNumber(int n){
		String number = String.valueOf(n);
		StringBuilder pretty = new StringBuilder();
		while(number.length() % 3 != 0){
			number = " " + number;
		}
		while(number.length() > 3){
			pretty.append(number.substring(0, 3));
			pretty.append(".");
			number = number.substring(3);
		}
		pretty.append(number);
		return pretty.toString().trim();
	}
	
	public static String ticks2time(int ticks){
		int cent = ticks % 20;
		int seconds = ticks / 20;
		cent *= 5;
		int minutes = seconds / 60;
		seconds -= minutes * 60;
		
		StringBuilder time = new StringBuilder();
		if(minutes > 0){
			time.append(minutes);
			time.append(":");
		}
		time.append(seconds);
		time.append(".");
		if(cent < 10){
			time.append("0");
		}
		time.append(cent);
		return time.toString();
	}
}