package lordfokas.stargatetech2;

import java.util.ArrayList;

import lordfokas.stargatetech2.lib.packet.BasePacket;
import lordfokas.stargatetech2.modules.IContentModule;
import lordfokas.stargatetech2.modules.ModuleAutomation;
import lordfokas.stargatetech2.modules.ModuleCore;
import lordfokas.stargatetech2.modules.ModuleEnemy;
import lordfokas.stargatetech2.modules.ModuleEnergy;
import lordfokas.stargatetech2.modules.ModuleIntegration;
import lordfokas.stargatetech2.modules.ModuleTransport;
import lordfokas.stargatetech2.modules.ModuleWorld;
import lordfokas.stargatetech2.reference.ModReference;
import lordfokas.stargatetech2.util.ChunkLoader;
import lordfokas.stargatetech2.util.Config;
import lordfokas.stargatetech2.util.IconRegistry;
import lordfokas.stargatetech2.util.Stacks;
import lordfokas.stargatetech2.util.StargateLogger;
import lordfokas.stargatetech2.util.api.APIImplementation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid=ModReference.MOD_ID, name=ModReference.MOD_NAME, version=ModReference.MOD_VERSION, dependencies=ModReference.DEPENDENCIES)
public class StargateTech2 {
	@Instance(ModReference.MOD_ID)
	public static StargateTech2 instance;
	
	@SidedProxy(clientSide="lordfokas.stargatetech2.ProxyClient", serverSide="lordfokas.stargatetech2.ProxyServer")
	public static ISidedProxy proxy;
	
	private ArrayList<IContentModule> modules = new ArrayList<IContentModule>();
	public static Config config;
	public static APIImplementation apiImplementation;
	
	public static ModuleCore core = new ModuleCore();
	public static ModuleAutomation automation = new ModuleAutomation();
	public static ModuleEnergy energy = new ModuleEnergy();
	public static ModuleEnemy enemy = new ModuleEnemy();
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
		modules.add(transport);
		modules.add(world);
		modules.add(integration);
		apiImplementation = new APIImplementation();
		MinecraftForge.EVENT_BUS.register(this);
		BasePacket.registerAll();
		
		StargateLogger.info("Pre-Initializing Modules");
		for(IContentModule module : modules){
			try{
				StargateLogger.info("Pre-Initializing Module: " + module.getModuleName());
				module.preInit();
			}catch(Exception e){
				StargateLogger.error("An error occurred while Pre-Initializing module \"" + module.getModuleName() + "\"");
				e.printStackTrace();
			}
		}
		StargateLogger.info("All Modules Pre-Initalized.");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		Stacks.init();
		ChunkLoader.register();
		StargateLogger.info("Initializing Modules");
		for(IContentModule module : modules){
			try{
				StargateLogger.info("Initializing Module: " + module.getModuleName());
				module.init();
			}catch(Exception e){
				StargateLogger.error("An error occurred while Initializing module \"" + module.getModuleName() + "\"");
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
				StargateLogger.error("An error occurred while Post-Initializing module \"" + module.getModuleName() + "\"");
				e.printStackTrace();
			}
		}
		StargateLogger.info("All Modules Post-Initalized.");
		proxy.registerRenderers(null);
		proxy.registerHandlers();
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
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void beforeTextureStitch(TextureStitchEvent.Pre event){
		IconRegistry.load(event.map);
	}
}