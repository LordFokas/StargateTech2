package lordfokas.stargatetech2.lib.tileentity;

public interface ISyncedGUI {
	public static interface Source extends ISyncedGUI{
		public int getValueCount();
		public int getValue(int key);
	}
	
	public static interface Sink extends ISyncedGUI{
		public void setValue(int key, int val);
	}
	
	public static interface Flow extends Source, Sink{}
}
