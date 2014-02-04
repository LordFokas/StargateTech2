package stargatetech2;

import java.util.ArrayList;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import stargatetech2.automation.ModuleAutomation;
import stargatetech2.core.ModuleCore;
import stargatetech2.core.api.APIImplementation;
import stargatetech2.core.reference.ModReference;
import stargatetech2.core.util.Config;
import stargatetech2.core.util.IconRegistry;
import stargatetech2.core.util.PacketHandler;
import stargatetech2.core.util.StargateLogger;
import stargatetech2.enemy.ModuleEnemy;
import stargatetech2.energy.ModuleEnergy;
import stargatetech2.factory.ModuleFactory;
import stargatetech2.integration.ModuleIntegration;
import stargatetech2.transport.ModuleTransport;
import stargatetech2.world.ModuleWorld;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid=ModReference.MOD_ID, name=ModReference.MOD_NAME, version=ModReference.MOD_VERSION, dependencies=ModReference.DEPENDENCIES)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class StargateTech2 {
	@Instance(ModReference.MOD_ID)
	public static StargateTech2 instance;
	
	@SidedProxy(clientSide="stargatetech2.ProxyClient", serverSide="stargatetech2.ProxyServer")
	public static ISidedProxy proxy;
	
	private ArrayList<IContentModule> modules = new ArrayList<IContentModule>();
	public static Config config;
	public static APIImplementation apiImplementation;
	
	public static ModuleCore core = new ModuleCore();
	public static ModuleAutomation automation = new ModuleAutomation();
	public static ModuleEnergy energy = new ModuleEnergy();
	public static ModuleEnemy enemy = new ModuleEnemy();
	public static ModuleFactory factory = new ModuleFactory();
	public static ModuleTransport transport = new ModuleTransport();
	public static ModuleWorld world = new ModuleWorld();
	public static ModuleIntegration integration = new ModuleIntegration();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
		modules.add(core);
		modules.add(automation);
		modules.add(enemy);
		modules.add(energy);
		modules.add(factory);
		modules.add(transport);
		modules.add(world);
		modules.add(integration);
		StargateLogger.init();
		apiImplementation = new APIImplementation();
		MinecraftForge.EVENT_BUS.register(this);
		PacketHandler.server.register();
		PacketHandler.client.register();
		
		StargateLogger.info("Pre-Initializing Modules");
		for(IContentModule module : modules){
			try{
				StargateLogger.info("Pre-Initializing Module: " + module.getModuleName());
				module.preInit();
			}catch(Exception e){
				StargateLogger.severe("An error occurred while Pre-Initializing module \"" + module.getModuleName() + "\"");
				e.printStackTrace();
			}
		}
		StargateLogger.info("All Modules Pre-Initalized.");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		StargateLogger.info("Initializing Modules");
		proxy.registerHandlers();
		for(IContentModule module : modules){
			try{
				StargateLogger.info("Initializing Module: " + module.getModuleName());
				module.init();
			}catch(Exception e){
				StargateLogger.severe("An error occurred while Initializing module \"" + module.getModuleName() + "\"");
				e.printStackTrace();
			}
		}
		StargateLogger.info("All Modules Initalized.");
		apiImplementation.enableExternalAccess();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		StargateLogger.info("Post-Initializing Modules");
		for(IContentModule module : modules){
			try{
				StargateLogger.info("Post-Initializing Module: " + module.getModuleName());
				module.postInit();
			}catch(Exception e){
				StargateLogger.severe("An error occurred while Post-Initializing module \"" + module.getModuleName() + "\"");
				e.printStackTrace();
			}
		}
		StargateLogger.info("All Modules Post-Initalized.");
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