package stargatetech2.common.reference;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;

public class TextureReference {
	public static final ArrayList<String> TERRAIN_TEXTURES = new ArrayList<String>(10);
	
	public static final String IONIZED_PARTICLES = "ionizedParticles";
	
	public static final String MACHINE_SIDE = "machineSide";
	public static final String MACHINE_TOP = "machineTop";
	public static final String MACHINE_BOTTOM = "machineBottom";
	
	public static final String FACE_PARTICLE_IONIZER = "faceParticleIonizer";
	public static final String GLOW_PARTICLE_IONIZER = "glowParticleIonizer";
	
	public static final String FACE_SHIELD_EMITTER = "faceShieldEmitter";
	public static final String GLOW_SHIELD_EMITTER = "glowShieldEmitter";
	
	static{
		TERRAIN_TEXTURES.add(IONIZED_PARTICLES);
		TERRAIN_TEXTURES.add(MACHINE_SIDE);
		TERRAIN_TEXTURES.add(MACHINE_TOP);
		TERRAIN_TEXTURES.add(MACHINE_BOTTOM);
		TERRAIN_TEXTURES.add(FACE_PARTICLE_IONIZER);
		TERRAIN_TEXTURES.add(GLOW_PARTICLE_IONIZER);
		TERRAIN_TEXTURES.add(FACE_SHIELD_EMITTER);
		TERRAIN_TEXTURES.add(FACE_SHIELD_EMITTER + "_B");
		TERRAIN_TEXTURES.add(FACE_SHIELD_EMITTER + "_D");
		TERRAIN_TEXTURES.add(FACE_SHIELD_EMITTER + "_T");
		TERRAIN_TEXTURES.add(GLOW_SHIELD_EMITTER);
		TERRAIN_TEXTURES.add(GLOW_SHIELD_EMITTER + "_B");
		TERRAIN_TEXTURES.add(GLOW_SHIELD_EMITTER + "_D");
		TERRAIN_TEXTURES.add(GLOW_SHIELD_EMITTER + "_T");
	}
	
	public static final ResourceLocation GUI_BASE = getTexture("gui/base.png");
	public static final ResourceLocation GUI_SHIELD_EMITTER = getTexture("gui/shieldEmitter.png");
	public static final ResourceLocation GUI_PARTICLE_IONIZER = getTexture("gui/particleIonizer.png");
	
	public static ResourceLocation getTexture(String texture){
		return new ResourceLocation(ModReference.MOD_ID + ":" + "textures/" + texture);
	}
}