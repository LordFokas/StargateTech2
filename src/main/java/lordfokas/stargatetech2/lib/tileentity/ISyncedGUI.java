package lordfokas.stargatetech2.lib.tileentity;

/**
 * These interfaces are implemented by {@link ITileContext} to
 * automate GUI Syncing.
 * 
 * @author LordFokas
 */
public interface ISyncedGUI {
	
	/**
	 * The interface an {@link ITileContext.Server} should implement.
	 * 
	 * @author LordFokas
	 */
	public static interface Source extends ISyncedGUI{
		public int getValueCount();
		public int getValue(int key);
	}
	
	/**
	 * The interface an {@link ITileContext.Client} should implement.
	 * 
	 * @author LordFokas
	 */
	public static interface Sink extends ISyncedGUI{
		public void setValue(int key, int val);
	}
	
	/**
	 * A mashup of both interfaces to alternatively be
	 * implemented by the common context, if any.
	 * 
	 * @author LordFokas
	 */
	public static interface Flow extends Source, Sink{}
}
