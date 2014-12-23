package lordfokas.stargatetech2.core;

import lordfokas.stargatetech2.core.reference.ModReference;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StargateLogger {
	private static Logger logger = LogManager.getLogger(ModReference.MOD_NAME);
	
	public static void log(Level level, String message) {
		logger.log(level, message);
	}
	
	public static void info(String msg){
		logger.log(Level.INFO, msg);
	}
	
	public static void warning(String msg){
		logger.log(Level.WARN, msg);
	}
	
	public static void error(String msg){
		logger.log(Level.ERROR, msg);
	}
}
