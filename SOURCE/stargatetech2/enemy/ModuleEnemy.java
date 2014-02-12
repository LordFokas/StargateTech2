package stargatetech2.enemy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import stargatetech2.IContentModule;
import stargatetech2.StargateTech2;
import stargatetech2.core.reference.TileEntityReference;
import stargatetech2.core.util.Stacks;
import stargatetech2.enemy.block.BlockParticleIonizer;
import stargatetech2.enemy.block.BlockShield;
import stargatetech2.enemy.block.BlockShieldEmitter;
import stargatetech2.enemy.item.ItemPersonalShield;
import stargatetech2.enemy.tileentity.TileParticleIonizer;
import stargatetech2.enemy.tileentity.TileShield;
import stargatetech2.enemy.tileentity.TileShieldEmitter;
import stargatetech2.enemy.util.EnemyEventHandler;
import stargatetech2.enemy.util.IonizedParticles;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModuleEnemy implements IContentModule {
	public static BlockShieldEmitter shieldEmitter;
	public static BlockParticleIonizer particleIonizer;
	public static BlockShield shield;
	
	public static ItemPersonalShield personalShield;
	
	@Override
	public void preInit(){
		shieldEmitter = new BlockShieldEmitter();
		particleIonizer = new BlockParticleIonizer();
		shield = new BlockShield();
		
		personalShield = new ItemPersonalShield();
	}

	@Override
	public void init(){
		FluidRegistry.registerFluid(IonizedParticles.fluid);
		shieldEmitter.registerBlock();
		particleIonizer.registerBlock();
		shield.registerBlock();
		
		GameRegistry.registerTileEntity(TileShieldEmitter.class, TileEntityReference.TILE_SHIELD_EMITTER);
		GameRegistry.registerTileEntity(TileParticleIonizer.class, TileEntityReference.TILE_PARTICLE_IONIZER);
		GameRegistry.registerTileEntity(TileShield.class, TileEntityReference.TILE_SHIELD);
	}

	@Override
	public void postInit(){
		LanguageRegistry.addName(shieldEmitter, "Shield Emitter");
		LanguageRegistry.addName(particleIonizer, "Particle Ionizer");
		LanguageRegistry.addName(shield, "Shield");
		LanguageRegistry.addName(personalShield, "Personal Shield");
		
		GameRegistry.addShapedRecipe(new ItemStack(shieldEmitter), "-B-", "SMS", "NCN", 'M', Stacks.machine, 'C', Stacks.coilNaq, 'B', Stacks.bucket, 'N', Stacks.naqIngot, 'S', Stacks.circuit);
		GameRegistry.addShapedRecipe(new ItemStack(particleIonizer), "-C-", "BMB", "NPN", 'P', Stacks.coilGold, 'C', Stacks.chest, 'B', Stacks.bucket, 'M', Stacks.machine, 'N', Stacks.naqIngot);
		
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