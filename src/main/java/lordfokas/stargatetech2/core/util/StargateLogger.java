package lordfokas.stargatetech2.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import lordfokas.stargatetech2.core.reference.ModReference;
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
