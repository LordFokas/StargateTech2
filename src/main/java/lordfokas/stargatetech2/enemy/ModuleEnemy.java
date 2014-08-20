package lordfokas.stargatetech2.enemy;

import lordfokas.stargatetech2.IContentModule;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.api.ParticleIonizerRecipes;
import lordfokas.stargatetech2.core.api.ParticleIonizerRecipes.IonizerRecipe;
import lordfokas.stargatetech2.core.reference.TileEntityReference;
import lordfokas.stargatetech2.core.util.Stacks;
import lordfokas.stargatetech2.enemy.block.BlockParticleIonizer;
import lordfokas.stargatetech2.enemy.block.BlockShield;
import lordfokas.stargatetech2.enemy.block.BlockShieldController;
import lordfokas.stargatetech2.enemy.block.BlockShieldEmitter;
import lordfokas.stargatetech2.enemy.item.ItemPersonalShield;
import lordfokas.stargatetech2.enemy.tileentity.TileParticleIonizer;
import lordfokas.stargatetech2.enemy.tileentity.TileShield;
import lordfokas.stargatetech2.enemy.tileentity.TileShieldController;
import lordfokas.stargatetech2.enemy.tileentity.TileShieldEmitter;
import lordfokas.stargatetech2.enemy.util.EnemyEventHandler;
import lordfokas.stargatetech2.enemy.util.IonizedParticles;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleEnemy implements IContentModule {
	public static BlockShieldController shieldController;
	public static BlockShieldEmitter shieldEmitter;
	public static BlockParticleIonizer particleIonizer;
	public static BlockShield shield;
	
	public static ItemPersonalShield personalShield;
	
	@Override
	public void preInit(){
		shieldController = new BlockShieldController();
		shieldEmitter = new BlockShieldEmitter();
		particleIonizer = new BlockParticleIonizer();
		shield = new BlockShield();
		
		personalShield = new ItemPersonalShield();
	}

	@Override
	public void init(){
		FluidRegistry.registerFluid(IonizedParticles.fluid);
		
		shieldController.registerBlock();
		shieldEmitter.registerBlock();
		particleIonizer.registerBlock();
		shield.registerBlock();
		
		GameRegistry.registerTileEntity(TileShieldController.class, TileEntityReference.TILE_SHIELD_CONTROLLER);
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
		GameRegistry.registerTileEntity(TileShield.class, TileEntityReference.TILE_SHIELD);
	}

	@Override
	public void postInit(){		
		GameRegistry.addShapedRecipe(new ItemStack(shieldController), "-B-", "CMC", "PAP", 'B', Stacks.bucket, 'C', Stacks.circuit, 'M', Stacks.machine, 'P', Stacks.naqPlate, 'A', Stacks.busCable);
		GameRegistry.addShapedRecipe(new ItemStack(shieldEmitter), "-B-", "SMS", "NCN", 'M', Stacks.machine, 'C', Stacks.coilNaq, 'B', Stacks.bucket, 'N', Stacks.naqIngot, 'S', Stacks.circuit);
		GameRegistry.addShapedRecipe(new ItemStack(particleIonizer), "-C-", "BMB", "NPN", 'P', Stacks.coilGold, 'C', Stacks.chest, 'B', Stacks.bucket, 'M', Stacks.machine, 'N', Stacks.naqIngot);
		
		Fluid water = FluidRegistry.WATER;
		ParticleIonizerRecipes.recipes().addRecipe(new IonizerRecipe(new FluidStack(water,  500), Stacks.redDust,    1200,  10, 10));
		ParticleIonizerRecipes.recipes().addRecipe(new IonizerRecipe(new FluidStack(water, 1500), Stacks.glowDust,  12000,   8, 15));
		ParticleIonizerRecipes.recipes().addRecipe(new IonizerRecipe(new FluidStack(water, 4500), Stacks.redBlock,   1200, 100, 200));
		ParticleIonizerRecipes.recipes().addRecipe(new IonizerRecipe(new FluidStack(water, 6000), Stacks.glowBlock, 12000,  32, 120));
		
		MinecraftForge.EVENT_BUS.register(new EnemyEventHandler());
		StargateTech2.proxy.registerRenderers(Module.ENEMY);
	}

	@Override public void onServerStart(){}
	@Override public void onServerStop(){}

	@Override
	public String getModuleName(){
		return "Enemy";
	}
}