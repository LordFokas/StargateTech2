package stargatetech2.core;

import buildcraft.BuildCraftTransport;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.common.machine.RenderBlockMachine;
import stargatetech2.common.reference.TileEntityReference;
import stargatetech2.core.block.BlockInvisible;
import stargatetech2.core.block.BlockTransportRing;
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
import stargatetech2.core.tileentity.TileTransportRing;
import stargatetech2.core.util.CoreEventHandler;
import stargatetech2.core.util.CoreWorldGenerator;
import stargatetech2.core.util.IonizedParticles;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public final class ModuleCore implements IContentModule{
	public static BlockShieldEmitter shieldEmitter;
	public static BlockParticleIonizer particleIonizer;
	public static BlockShield shield;
	public static BlockNaquadahRail naquadahRail;
	public static BlockNaquadahOre naquadahOre;
	public static BlockTransportRing transportRing;
	public static BlockInvisible invisible;
	
	public static ItemTabletPC tabletPC;
	public static ItemNaquadahIngot naquadahIngot;
	
	@Override
	public void preInit(){
		shieldEmitter = new BlockShieldEmitter();
		particleIonizer = new BlockParticleIonizer();
		shield = new BlockShield();
		naquadahRail = new BlockNaquadahRail();
		naquadahOre = new BlockNaquadahOre();
		transportRing = new BlockTransportRing();
		invisible = new BlockInvisible();
		
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
		transportRing.registerBlock();
		invisible.registerBlock();
		
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
		GameRegistry.registerTileEntity(TileShield.class, TileEntityReference.TILE_SHIELD);
		GameRegistry.registerTileEntity(TileTransportRing.class, TileEntityReference.TILE_TRANSPORT_RING);
	}

	@Override
	public void postInit(){
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
		
		LanguageRegistry.addName(shieldEmitter, "Shield Emitter");
		LanguageRegistry.addName(particleIonizer, "Particle Ionizer");
		LanguageRegistry.addName(shield, "Shield");
		LanguageRegistry.addName(naquadahRail, "Naquadah Rail");
		LanguageRegistry.addName(naquadahOre, "Naquadah Ore");
		LanguageRegistry.addName(transportRing, "Transport Ring");
		LanguageRegistry.addName(invisible, "Invisible Block");
		
		LanguageRegistry.addName(tabletPC, "Tablet PC");
		LanguageRegistry.addName(naquadahIngot, "Naquadah Ingot");
		
		StargateTech2.proxy.registerRenderers(Module.CORE);
		
		GameRegistry.registerWorldGenerator(new CoreWorldGenerator());
		
		addCoreRecipes();
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName() {
		return "Core";
	}
	
	private void addCoreRecipes(){
		ItemStack naquadah = new ItemStack(naquadahIngot);
		ItemStack glass = new ItemStack(Block.thinGlass);
		ItemStack stone = new ItemStack(Block.stone);
		ItemStack fPipe = new ItemStack(BuildCraftTransport.pipeFluidsGold);
		ItemStack kPipe = new ItemStack(BuildCraftTransport.pipePowerGold);
		ItemStack cauldron = new ItemStack(Item.cauldron);
		ItemStack stick = new ItemStack(Item.stick);
		ItemStack redstone = new ItemStack(Item.redstone);
		
		GameRegistry.addSmelting(naquadahOre.blockID, naquadah, 0);
		
		GameRegistry.addShapedRecipe(new ItemStack(shieldEmitter), "SNS", "NGN", "SPS", 'S', stone, 'N', naquadah, 'G', glass, 'P', fPipe);
		GameRegistry.addShapedRecipe(new ItemStack(particleIonizer), "SKS", "NCN", "SFS", 'S', stone, 'K', kPipe, 'N', naquadah, 'C', cauldron, 'F', fPipe);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahRail), "NSN", "NSN", "NSN", 'N', naquadah, 'S', stick);
		GameRegistry.addShapedRecipe(new ItemStack(tabletPC), "NNN", "RGR", "NNN", 'N', naquadah, 'R', redstone, 'G', glass);
	}
}