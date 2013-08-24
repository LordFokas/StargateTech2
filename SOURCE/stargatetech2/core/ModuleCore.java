package stargatetech2.core;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.common.machine.RenderBlockMachine;
import stargatetech2.common.reference.TileEntityReference;
import stargatetech2.core.block.BlockNaquadahOre;
import stargatetech2.core.block.BlockNaquadahRail;
import stargatetech2.core.block.BlockParticleIonizer;
import stargatetech2.core.block.BlockShield;
import stargatetech2.core.block.BlockShieldEmitter;
import stargatetech2.core.item.ItemNaquadahIngot;
import stargatetech2.core.item.ItemTabletPC;
import stargatetech2.core.rendering.RenderNaquadahOre;
import stargatetech2.core.rendering.RenderNaquadahRail;
import stargatetech2.core.tileentity.TileParticleIonizer;
import stargatetech2.core.tileentity.TileShield;
import stargatetech2.core.tileentity.TileShieldEmitter;
import stargatetech2.core.util.CoreEventHandler;
import stargatetech2.core.util.IonizedParticles;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public final class ModuleCore implements IContentModule{
	public static BlockShieldEmitter shieldEmitter;
	public static BlockParticleIonizer particleIonizer;
	public static BlockShield shield;
	public static BlockNaquadahRail naquadahRail;
	public static BlockNaquadahOre naquadahOre;
	
	public static ItemTabletPC tabletPC;
	public static ItemNaquadahIngot naquadahIngot;
	
	@Override
	public void preInit(){
		shieldEmitter = new BlockShieldEmitter();
		particleIonizer = new BlockParticleIonizer();
		shield = new BlockShield();
		naquadahRail = new BlockNaquadahRail();
		naquadahOre = new BlockNaquadahOre();
		
		tabletPC = new ItemTabletPC();
		naquadahIngot = new ItemNaquadahIngot();
	}

	@Override
	public void init(){
		FluidRegistry.registerFluid(IonizedParticles.fluid);
		
		shieldEmitter.registerBlock();
		particleIonizer.registerBlock();
		shield.registerBlock();
		naquadahRail.registerBlock();
		naquadahOre.registerBlock();
		
		GameRegistry.addSmelting(naquadahOre.blockID, new ItemStack(naquadahIngot, 1), 0);
		
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
		GameRegistry.registerTileEntity(TileShield.class, TileEntityReference.TILE_SHIELD);
	}

	@Override
	public void postInit(){
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
		
		LanguageRegistry.addName(shieldEmitter, "Shield Emitter");
		LanguageRegistry.addName(particleIonizer, "Particle Ionizer");
		LanguageRegistry.addName(shield, "Shield");
		LanguageRegistry.addName(naquadahRail, "Naquadah Rail");
		LanguageRegistry.addName(naquadahOre, "Naquadah Ore");
		
		LanguageRegistry.addName(tabletPC, "Tablet PC");
		LanguageRegistry.addName(naquadahIngot, "Naquadah Ingot");
		
		StargateTech2.instance.proxy.registerRenderer(RenderBlockMachine.instance());
		StargateTech2.instance.proxy.registerRenderer(RenderNaquadahRail.instance());
		StargateTech2.instance.proxy.registerRenderer(RenderNaquadahOre.instance());
	}

	@Override
	public void onServerStart(){}

	@Override
	public void onServerStop(){}

	@Override
	public String getModuleName() {
		return "Core";
	}
}