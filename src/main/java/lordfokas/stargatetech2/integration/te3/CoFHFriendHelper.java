package lordfokas.stargatetech2.integration.te3;

import java.lang.reflect.Method;

import lordfokas.stargatetech2.core.StargateLogger;

/**
 * This is mainly a huge pile of Reflection hackery to
 * gain access to the CoFHSocial Friend system, since
 * there is no public API to access it...
 * 
 * @author LordFokas
 */
public final class CoFHFriendHelper {
	private static final String CLASS_NAME = "cofh.social.RegistryFriends";
	private static final String METHOD_NAME = "playerHasAccess";
	private static Method method = null;
	
	public static void init(){
		try{
			Class friends = Class.forName(CLASS_NAME);
			Method[] methods = friends.getMethods();
			for(Method m : methods){
				if(m.getName().equals(METHOD_NAME)){
					method = m;
					StargateLogger.info("Found CoFH Friend Registry: " + CLASS_NAME + "->" + METHOD_NAME + "()");
					return;
				}
			}
			StargateLogger.info("Unable to find CoFH Friend Registry: " + CLASS_NAME + "->" + METHOD_NAME + "()");
		}catch(Exception e){
			StargateLogger.error("Error while attempting to find CoFH Friend Registry:");
			e.printStackTrace();
		}
	}
	
	public static boolean isFriend(String player, String owner){
		if(method != null && player != null && owner != null){
			try{
				return (Boolean) method.invoke(null, player, owner);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean isSystemEnabled(){
		return method != null;
	}
}