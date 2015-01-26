package lordfokas.stargatetech2.modules;

import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.modules.enemy.BlockParticleIonizer;
import lordfokas.stargatetech2.modules.enemy.BlockShield;
import lordfokas.stargatetech2.modules.enemy.BlockShieldController;
import lordfokas.stargatetech2.modules.enemy.BlockShieldEmitter;
import lordfokas.stargatetech2.modules.enemy.EnemyEventHandler;
import lordfokas.stargatetech2.modules.enemy.IonizedParticles;
import lordfokas.stargatetech2.modules.enemy.ItemPersonalShield;
import lordfokas.stargatetech2.modules.enemy.TileParticleIonizer;
import lordfokas.stargatetech2.modules.enemy.TileShield;
import lordfokas.stargatetech2.modules.enemy.TileShieldController;
import lordfokas.stargatetech2.modules.enemy.TileShieldEmitter;
import lordfokas.stargatetech2.reference.ItemReference;
import lordfokas.stargatetech2.reference.TileEntityReference;
import lordfokas.stargatetech2.util.Stacks;
import lordfokas.stargatetech2.util.api.ParticleIonizerRecipes;
import lordfokas.stargatetech2.util.api.ParticleIonizerRecipes.IonizerRecipe;
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
		GameRegistry.registerItem(personalShield, ItemReference.PERSONAL_SHIELD);
		GameRegistry.registerTileEntity(TileShieldController.class, TileEntityReference.TILE_SHIELD_CONTROLLER);
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
		GameRegistry.registerTileEntity(TileShield.class, TileEntityReference.TILE_SHIELD);
	}

	@Override
	public void postInit(){		
		GameRegistry.addShapedRecipe(new ItemStack(shieldController), "-B-", "CMC", "PAP", 'B', Stacks.bucket, 'C', Stacks.circuit, 'M', Stacks.machine_0, 'P', Stacks.naqPlate, 'A', Stacks.busCable);
		GameRegistry.addShapedRecipe(new ItemStack(shieldEmitter), "-B-", "SMS", "NCN", 'M', Stacks.machine_0, 'C', Stacks.coilNaq, 'B', Stacks.bucket, 'N', Stacks.naqIngot, 'S', Stacks.circuit);
		GameRegistry.addShapedRecipe(new ItemStack(particleIonizer), "-C-", "BMB", "NPN", 'P', Stacks.coilGold, 'C', Stacks.chest, 'B', Stacks.bucket, 'M', Stacks.machine_0, 'N', Stacks.naqIngot);
		
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