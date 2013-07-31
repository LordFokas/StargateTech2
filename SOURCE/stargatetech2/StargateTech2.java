package stargatetech2;

import java.util.ArrayList;

import net.minecraftforge.common.Configuration;
import stargatetech2.common.reference.ModReference;
import stargatetech2.common.util.APIImplementation;
import stargatetech2.common.util.Config;
import stargatetech2.common.util.StargateLogger;
import stargatetech2.core.ModuleCore;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerAboutToStart;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid=ModReference.MOD_ID, name=ModReference.MOD_NAME, version=ModReference.MOD_VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class StargateTech2 {
	@Instance(ModReference.MOD_ID)
	public static StargateTech2 instance;
	
	@SidedProxy(clientSide="stargatetech2.ProxyClient", serverSide="stargatetech2.ProxyServer")
	public static ISidedProxy proxy;
	
	private ArrayList<IContentModule> modules = new ArrayList<IContentModule>();
	public Config config;
	public APIImplementation apiImplementation;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event){
		config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
		modules.add(new ModuleCore());
		StargateLogger.init();
		apiImplementation = new APIImplementation();
		
		StargateLogger.info("Pre-Initializing Modules");
		for(IContentModule module : modules){
			try{
				module.preInit();
			}catch(Exception e){
				StargateLogger.severe("An error occurred when Pre-Initializing module \"" + module.getModuleName() + "\"");
				e.printStackTrace();
			}
		}
		StargateLogger.info("All Modules Pre-Initalized successfully");
	}
	
	@Init
	public void init(FMLInitializationEvent event){
		StargateLogger.info("Initializing Modules");
		for(IContentModule module : modules){
			try{
				module.init();
			}catch(Exception e){
				StargateLogger.severe("An error occurred when Initializing module \"" + module.getModuleName() + "\"");
				e.printStackTrace();
			}
		}
		StargateLogger.info("All Modules Initalized successfully");
		apiImplementation.enableExternalAccess();
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event){
		StargateLogger.info("Post-Initializing Modules");
		for(IContentModule module : modules){
			try{
				module.postInit();
			}catch(Exception e){
				StargateLogger.severe("An error occurred when Post-Initializing module \"" + module.getModuleName() + "\"");
				e.printStackTrace();
			}
		}
		StargateLogger.info("All Modules Post-Initalized successfully");
		config.save();
	}
	
	@ServerAboutToStart
	public void onServerStart(FMLServerAboutToStartEvent event){
		for(IContentModule module : modules){
			module.onServerStart();
		}
	}
	
	@ServerStopping
	public void onServerStop(FMLServerStoppingEvent event){
		for(IContentModule module : modules){
			module.onServerStop();
		}
	}
}