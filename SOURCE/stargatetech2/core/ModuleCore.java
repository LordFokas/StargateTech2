package stargatetech2.core;

import cpw.mods.fml.common.registry.GameRegistry;
import stargatetech2.IContentModule;
import stargatetech2.core.blocks.SupportFrame;

public final class ModuleCore implements IContentModule{
	public SupportFrame supportFrame;
	
	@Override
	public void preInit(){
		supportFrame = new SupportFrame();
		GameRegistry.registerBlock(supportFrame, supportFrame.getUnlocalizedName());
	}

	@Override
	public void init(){}

	@Override
	public void postInit(){}

	@Override
	public void onServerStart(){}

	@Override
	public void onServerStop(){}

	@Override
	public String getModuleName() {
		return "Core";
	}
}