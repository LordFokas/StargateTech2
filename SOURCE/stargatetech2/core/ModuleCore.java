package stargatetech2.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.common.reference.TileEntityReference;
import stargatetech2.common.util.Color;
import stargatetech2.core.block.BlockInvisible;
import stargatetech2.core.block.BlockLanteanWall;
import stargatetech2.core.block.BlockNaquadahCapacitor;
import stargatetech2.core.block.BlockNaquadahOre;
import stargatetech2.core.block.BlockNaquadahRail;
import stargatetech2.core.block.BlockParticleIonizer;
import stargatetech2.core.block.BlockShield;
import stargatetech2.core.block.BlockShieldEmitter;
import stargatetech2.core.block.BlockTransportRing;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.core.item.ItemPersonalShield;
import stargatetech2.core.item.ItemTabletPC;
import stargatetech2.core.tileentity.TileNaquadahCapacitor;
import stargatetech2.core.tileentity.TileParticleIonizer;
import stargatetech2.core.tileentity.TileShield;
import stargatetech2.core.tileentity.TileShieldEmitter;
import stargatetech2.core.tileentity.TileTransportRing;
import stargatetech2.core.util.CoreEventHandler;
import stargatetech2.core.util.IonizedParticles;
import stargatetech2.core.worldgen.CoreWorldGenerator;
import buildcraft.BuildCraftTransport;
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
	public static BlockLanteanWall lanteanWall;
	public static BlockNaquadahCapacitor naquadahCapacitor;
	
	public static ItemTabletPC tabletPC;
	public static ItemPersonalShield personalShield;
	public static ItemNaquadah naquadah;
	
	@Override
	public void preInit(){
		shieldEmitter = new BlockShieldEmitter();
		particleIonizer = new BlockParticleIonizer();
		shield = new BlockShield();
		naquadahRail = new BlockNaquadahRail();
		naquadahOre = new BlockNaquadahOre();
		transportRing = new BlockTransportRing();
		invisible = new BlockInvisible();
		lanteanWall = new BlockLanteanWall();
		naquadahCapacitor = new BlockNaquadahCapacitor();
		
		tabletPC = new ItemTabletPC();
		personalShield = new ItemPersonalShield();
		naquadah = new ItemNaquadah();
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
		lanteanWall.registerBlock();
		naquadahCapacitor.registerBlock();
		
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
		GameRegistry.registerTileEntity(TileShield.class, TileEntityReference.TILE_SHIELD);
		GameRegistry.registerTileEntity(TileTransportRing.class, TileEntityReference.TILE_TRANSPORT_RING);
		GameRegistry.registerTileEntity(TileNaquadahCapacitor.class, TileEntityReference.TILE_NAQUADAH_CAPACITOR);
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
		for(int i = 0; i < 16; i++)
			LanguageRegistry.addName(new ItemStack(lanteanWall, 1, i), Color.COLORS[i].name + " Lantean Wall");
		LanguageRegistry.addName(naquadahCapacitor, "Naquadah Capacitor");
		
		LanguageRegistry.addName(tabletPC, "Tablet PC");
		LanguageRegistry.addName(personalShield, "Personal Shield");
		String[] names = naquadah.getItemNames();
		for(int i = 0; i < names.length; i++){
			LanguageRegistry.addName(new ItemStack(naquadah, 1, i), names[i]);
		}
		
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
		ItemStack naqIngot = new ItemStack(naquadah, 1, 0);
		ItemStack naqDust = new ItemStack(naquadah, 1, 1);
		ItemStack naqBar = new ItemStack(naquadah, 1, 2);
		ItemStack naqPlate = new ItemStack(naquadah, 1, 3);
		ItemStack glass = new ItemStack(Block.thinGlass);
		ItemStack stone = new ItemStack(Block.stone);
		ItemStack fPipe = new ItemStack(BuildCraftTransport.pipeFluidsGold);
		ItemStack kPipe = new ItemStack(BuildCraftTransport.pipePowerGold);
		ItemStack cauldron = new ItemStack(Item.cauldron);
		ItemStack stick = new ItemStack(Item.stick);
		ItemStack redstone = new ItemStack(Item.redstone);
		ItemStack pearl = new ItemStack(Item.enderPearl);
		ItemStack ironBlock = new ItemStack(Block.blockIron);
		
		GameRegistry.addSmelting(naquadahOre.blockID, naqIngot, 0);
		
		GameRegistry.addShapedRecipe(new ItemStack(shieldEmitter), "SNS", "NGN", "SPS", 'S', stone, 'N', naqPlate, 'G', glass, 'P', fPipe);
		GameRegistry.addShapedRecipe(new ItemStack(particleIonizer), "SKS", "NCN", "SFS", 'S', stone, 'K', kPipe, 'N', naqPlate, 'C', cauldron, 'F', fPipe);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahRail), "NSN", "NSN", "NSN", 'N', naqBar, 'S', stick);
		GameRegistry.addShapedRecipe(new ItemStack(tabletPC), "NNN", "RGR", "NNN", 'N', naqIngot, 'R', redstone, 'G', glass);
		GameRegistry.addShapedRecipe(new ItemStack(transportRing), "NPN", "NBN", "NPN", 'N', naqPlate, 'P', pearl, 'B', ironBlock);
		GameRegistry.addShapedRecipe(new ItemStack(lanteanWall, 8, Color.LIGHT_GRAY.id), "SSS", "SNS", "SSS", 'S', stone, 'N', naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadah, 4, 2), "--S", "-S-", "S--", 'S', naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadah, 2, 3), "SS", "SS", 'S', naqIngot);
		
		GameRegistry.addShapelessRecipe(new ItemStack(naquadah, 2, 1), new ItemStack(naquadahOre));
		
		for(Color color : Color.COLORS){
			for(int i = 0; i < 16; i++){
				if(i != color.id){
					GameRegistry.addShapelessRecipe(new ItemStack(lanteanWall, 1, color.id), new ItemStack(lanteanWall, 1, i), color.getDye());
				}
			}
		}
	}
}