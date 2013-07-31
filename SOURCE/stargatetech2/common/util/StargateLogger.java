package stargatetech2.common.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import stargatetech2.common.reference.ModReference;

import cpw.mods.fml.common.FMLLog;

public class StargateLogger {
	private static Logger logger = Logger.getLogger(ModReference.MOD_ID);

	public static void init() {
		logger.setParent(FMLLog.getLogger());
	}
	
	public static void log(Level level, String message) {
		logger.log(level, message);
	}
	
	public static void config(String msg){
		logger.log(Level.CONFIG, msg);
	}
	
	public static void info(String msg){
		logger.log(Level.INFO, msg);
	}
	
	public static void warning(String msg){
		logger.log(Level.WARNING, msg);
	}
	
	public static void severe(String msg){
		logger.log(Level.SEVERE, msg);
	}
}
