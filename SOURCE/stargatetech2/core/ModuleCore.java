package stargatetech2.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.common.reference.TileEntityReference;
import stargatetech2.common.util.Color;
import stargatetech2.core.block.BlockBusCable;
import stargatetech2.core.block.BlockInvisible;
import stargatetech2.core.block.BlockLanteanWall;
import stargatetech2.core.block.BlockNaquadahOre;
import stargatetech2.core.block.BlockNaquadahRail;
import stargatetech2.core.block.BlockParticleIonizer;
import stargatetech2.core.block.BlockShield;
import stargatetech2.core.block.BlockShieldEmitter;
import stargatetech2.core.block.BlockStargate;
import stargatetech2.core.block.BlockTransportRing;
import stargatetech2.core.item.ItemNaquadah;
import stargatetech2.core.item.ItemPersonalShield;
import stargatetech2.core.item.ItemTabletPC;
import stargatetech2.core.network.stargate.StargateNetwork;
import stargatetech2.core.tileentity.TileParticleIonizer;
import stargatetech2.core.tileentity.TileShield;
import stargatetech2.core.tileentity.TileShieldEmitter;
import stargatetech2.core.tileentity.TileStargate;
import stargatetech2.core.tileentity.TileStargateBase;
import stargatetech2.core.tileentity.TileStargateRing;
import stargatetech2.core.tileentity.TileTransportRing;
import stargatetech2.core.util.ChunkLoader;
import stargatetech2.core.util.CoreEventHandler;
import stargatetech2.core.util.IonizedParticles;
import stargatetech2.core.worldgen.CoreWorldGenerator;
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
	public static BlockStargate stargate;
	public static BlockBusCable busCable;
	
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
		stargate = new BlockStargate();
		busCable = new BlockBusCable();
		
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
		stargate.registerBlock();
		busCable.registerBlock();
		
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
		GameRegistry.registerTileEntity(TileShield.class, TileEntityReference.TILE_SHIELD);
		GameRegistry.registerTileEntity(TileTransportRing.class, TileEntityReference.TILE_TRANSPORT_RING);
		GameRegistry.registerTileEntity(TileStargate.class, TileEntityReference.TILE_STARGATE);
		GameRegistry.registerTileEntity(TileStargateRing.class, TileEntityReference.TILE_STARGATE_RING);
		GameRegistry.registerTileEntity(TileStargateBase.class, TileEntityReference.TILE_STARGATE_BASE);
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
		LanguageRegistry.addName(stargate, "Stargate");
		LanguageRegistry.addName(busCable, "Abstract Bus Cable");
		
		LanguageRegistry.addName(tabletPC, "Tablet PC");
		LanguageRegistry.addName(personalShield, "Personal Shield");
		String[] names = naquadah.getItemNames();
		for(int i = 0; i < names.length; i++){
			LanguageRegistry.addName(new ItemStack(naquadah, 1, i), names[i]);
		}
		
		StargateTech2.proxy.registerRenderers(Module.CORE);
		GameRegistry.registerWorldGenerator(new CoreWorldGenerator());
		ChunkLoader.register();
		
		addCoreRecipes();
	}

	@Override public void onServerStart(){
		ChunkLoader.load();
		StargateNetwork.instance().load();
	}
	
	@Override public void onServerStop(){
		StargateNetwork.instance().unload();
		ChunkLoader.unload();
	}

	@Override
	public String getModuleName() {
		return "Core";
	}
	
	private void addCoreRecipes(){
		ItemStack naqIngot = new ItemStack(naquadah, 1, ItemNaquadah.INGOT.ID);
		ItemStack naqDust = new ItemStack(naquadah, 1, ItemNaquadah.DUST.ID);
		ItemStack naqBar = new ItemStack(naquadah, 1, ItemNaquadah.BAR.ID);
		ItemStack naqPlate = new ItemStack(naquadah, 1, ItemNaquadah.PLATE.ID);
		ItemStack lattice = new ItemStack(naquadah, 1, ItemNaquadah.LATTICE.ID);
		ItemStack circuit = new ItemStack(naquadah, 1, ItemNaquadah.CIRCUIT.ID);
		ItemStack crystal1 = new ItemStack(naquadah, 1, ItemNaquadah.PWCR1.ID);
		ItemStack crystal2 = new ItemStack(naquadah, 1, ItemNaquadah.PWCR2.ID);
		ItemStack crystal3 = new ItemStack(naquadah, 1, ItemNaquadah.PWCR3.ID);
		
		ItemStack glass = new ItemStack(Block.thinGlass);
		ItemStack stone = new ItemStack(Block.stone);
		ItemStack cauldron = new ItemStack(Item.cauldron);
		ItemStack stick = new ItemStack(Item.stick);
		ItemStack redstone = new ItemStack(Item.redstone);
		ItemStack pearl = new ItemStack(Item.enderPearl);
		ItemStack ironBlock = new ItemStack(Block.blockIron);
		ItemStack quartz = new ItemStack(Item.netherQuartz);
		ItemStack diamond = new ItemStack(Item.diamond);
		ItemStack bucket = new ItemStack(Item.bucketEmpty);
		
		GameRegistry.addSmelting(naquadahOre.blockID, naqIngot, 0);
		FurnaceRecipes.smelting().addSmelting(naquadah.itemID, ItemNaquadah.LATTICE.ID, circuit, 0);
		FurnaceRecipes.smelting().addSmelting(naquadah.itemID, ItemNaquadah.DUST.ID, naqIngot, 0);
		
		GameRegistry.addShapedRecipe(new ItemStack(shieldEmitter), "PBP", "CPC", "PCP", 'P', naqPlate, 'C', circuit, 'B', bucket);
		GameRegistry.addShapedRecipe(new ItemStack(particleIonizer), "PBP", "CPC", "PYP", 'P', naqPlate, 'C', circuit, 'B', bucket, 'Y', crystal1);
		GameRegistry.addShapedRecipe(new ItemStack(naquadahRail), "NSN", "NSN", "NSN", 'N', naqBar, 'S', stick);
		GameRegistry.addShapedRecipe(new ItemStack(tabletPC), "NNN", "RGR", "NNN", 'N', naqIngot, 'R', redstone, 'G', glass);
		GameRegistry.addShapedRecipe(new ItemStack(transportRing), "NPN", "NBN", "NPN", 'N', naqPlate, 'P', pearl, 'B', ironBlock);
		GameRegistry.addShapedRecipe(new ItemStack(lanteanWall, 8, Color.LIGHT_GRAY.id), "SSS", "SNS", "SSS", 'S', stone, 'N', naqIngot);
		
		GameRegistry.addShapelessRecipe(new ItemStack(naquadah, 3, ItemNaquadah.LATTICE.ID), quartz, quartz, naqDust);
		GameRegistry.addShapedRecipe(new ItemStack(naquadah, 4, ItemNaquadah.BAR.ID), "--S", "-S-", "S--", 'S', naqIngot);
		GameRegistry.addShapedRecipe(new ItemStack(naquadah, 2, ItemNaquadah.PLATE.ID), "SS", "SS", 'S', naqIngot);
		GameRegistry.addShapedRecipe(crystal1, "GNG", "NNN", "CNC", 'N', naqIngot, 'C', circuit, 'G', Color.GREEN.getDye());
		GameRegistry.addShapedRecipe(crystal2, "YNY", "NGN", "CDC", 'N', naqIngot, 'C', circuit, 'G', crystal1, 'D', diamond, 'Y', Color.YELLOW.getDye());
		GameRegistry.addShapedRecipe(crystal3, "RNR", "NYN", "CDC", 'N', naqIngot, 'C', circuit, 'Y', crystal2, 'D', diamond, 'R', Color.RED.getDye());
		GameRegistry.addShapedRecipe(new ItemStack(busCable, 8), "WWW", "NNN", "WWW", 'N', naqIngot, 'W', new ItemStack(Block.cloth, 1, OreDictionary.WILDCARD_VALUE));
		
		for(Color color : Color.COLORS){
			for(int i = 0; i < 16; i++){
				if(i != color.id){
					GameRegistry.addShapelessRecipe(new ItemStack(lanteanWall, 1, color.id), new ItemStack(lanteanWall, 1, i), color.getDye());
				}
			}
		}
	}
}