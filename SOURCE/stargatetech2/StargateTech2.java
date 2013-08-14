package stargatetech2;

import java.util.ArrayList;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import stargatetech2.common.APIImplementation;
import stargatetech2.common.reference.ModReference;
import stargatetech2.common.registry.IconRegistry;
import stargatetech2.common.util.Config;
import stargatetech2.common.util.GUIHandler;
import stargatetech2.common.util.PacketHandler;
import stargatetech2.common.util.StargateLogger;
import stargatetech2.core.ModuleCore;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	
	public ModuleCore core;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
		core = new ModuleCore();
		modules.add(core);
		StargateLogger.init();
		apiImplementation = new APIImplementation();
		MinecraftForge.EVENT_BUS.register(this);
		NetworkRegistry.instance().registerGuiHandler(this, new GUIHandler());
		PacketHandler.server.register();
		PacketHandler.client.register();
		
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
	
	@EventHandler
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
	
	@EventHandler
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
	
	@EventHandler
	public void onServerStart(FMLServerAboutToStartEvent event){
		for(IContentModule module : modules){
			module.onServerStart();
		}
	}
	
	@EventHandler
	public void onServerStop(FMLServerStoppingEvent event){
		for(IContentModule module : modules){
			module.onServerStop();
		}
	}
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void beforeTextureStitch(TextureStitchEvent.Pre event){
		IconRegistry.load(event.map);
	}
}