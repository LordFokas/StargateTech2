package lordfokas.stargatetech2.lib.block;

public enum BlockRenderType {
	STANDARD(3),
	TESR_ONLY(2),
	VANILLA_FLUID(1),
	NO_RENDER(-1);
	
	public final int value;
	
	private BlockRenderType(int value){
		this.value = value;
	}
}