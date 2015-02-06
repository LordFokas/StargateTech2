package lordfokas.stargatetech2.lib.tileentity.faces;

import lordfokas.stargatetech2.reference.TextureReference;

public enum FaceColor{
	BLUE,	// In:	Main
	PURPLE,	// In:	1st
	GREEN,	// In:	2nd
	ORANGE,	// Out:	Main
	YELLOW,	// Out:	1st
	RED,	// Out:	2nd
	STRIPES,
	VOID;
	
	public String getTexture(){
		switch(this){
			case BLUE:		return TextureReference.INTERFACE_BLUE;
			case GREEN:		return TextureReference.INTERFACE_GREEN;
			case ORANGE:	return TextureReference.INTERFACE_ORANGE;
			case PURPLE:	return TextureReference.INTERFACE_PURPLE;
			case RED:		return TextureReference.INTERFACE_RED;
			case STRIPES:	return TextureReference.INTERFACE_STRIPES;
			case YELLOW:	return TextureReference.INTERFACE_YELLOW;
			default:		return TextureReference.TEXTURE_INVISIBLE;
		}
	}
	
	public boolean isInput(){
		return this == BLUE || this == GREEN || this == PURPLE || this == STRIPES;
	}
	
	public boolean isOutput(){
		return this == ORANGE || this == RED || this == YELLOW || this == STRIPES;
	}
	
	public boolean isMain(){
		return this == BLUE || this == ORANGE || this == STRIPES;
	}
	
	public boolean isPrimary(){
		return this == PURPLE || this == YELLOW || this == STRIPES;
	}
	
	public boolean isSecondary(){
		return this == GREEN || this == RED || this == STRIPES;
	}
	
	public boolean isUniversal(){
		return this == STRIPES;
	}
	
	public boolean isColored(){
		return this != VOID;
	}
}