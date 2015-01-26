package lordfokas.stargatetech2.util;

import java.io.File;
import java.util.LinkedList;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

public class Helper {
	public static ForgeDirection yaw2dir(float yaw, float pitch, boolean vertical){
		if(vertical){
			if(pitch < -45) return ForgeDirection.DOWN;
			if(pitch >  45) return ForgeDirection.UP;
		}
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
	
	public static File getFile(String file){
		return new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + File.separator + "SGTech2_" + file);
	}
	
	public static String prettyNumber(int n){
		String number = String.valueOf(n);
		LinkedList<String> parts = new LinkedList();
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