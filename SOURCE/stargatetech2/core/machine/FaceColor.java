package stargatetech2.core.machine;

import stargatetech2.core.reference.TextureReference;

public enum FaceColor{
	BLUE,	// In:	Main	FIRST
	PURPLE,	// In:	1st
	GREEN,	// In:	2nd		SECOND
	ORANGE,	// Out:	Main
	YELLOW,	// Out:	1st
	RED,	// Out:	2nd		THIRD
	VOID;
	
	public boolean isColor(){
		return this != VOID;
	}
	
	public String getTexture(){
		switch(this){
			case BLUE:		return TextureReference.INTERFACE_BLUE;
			case GREEN:		return TextureReference.INTERFACE_GREEN;
			case ORANGE:	return TextureReference.INTERFACE_ORANGE;
			case PURPLE:	return TextureReference.INTERFACE_PURPLE;
			case RED:		return TextureReference.INTERFACE_RED;
			case YELLOW:	return TextureReference.INTERFACE_YELLOW;
			default:		return TextureReference.TEXTURE_INVISIBLE;
		}
	}
	
	public boolean isInput(){
		return this == BLUE || this == GREEN || this == PURPLE;
	}
	
	public boolean isOutput(){
		return this == ORANGE || this == RED || this == YELLOW;
	}
}
