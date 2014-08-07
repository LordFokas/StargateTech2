package lordfokas.stargatetech2.core.machine.tabs;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import lordfokas.stargatetech2.core.base.BaseTab;
import lordfokas.stargatetech2.core.machine.Face;
import lordfokas.stargatetech2.core.machine.FaceColor;
import lordfokas.stargatetech2.core.machine.TileMachine;
import lordfokas.stargatetech2.core.packet.PacketToggleMachineFace;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.core.util.IconRegistry;
import lordfokas.stargatetech2.core.util.Stacks;

public class TabConfiguration extends BaseTab{
	private static final Side[] sides = new Side[]{
		new Side(Face.BOTTOM, 45, 61),
		new Side(Face.TOP, 45, 25),
		new Side(Face.LEFT, 27, 43),
		new Side(Face.FRONT, 45, 43),
		new Side(Face.RIGHT, 63, 43),
		new Side(Face.BACK, 63, 61)
	};
	
	private static class Side{
		public final int x, y;
		public final Face face;
		
		public Side(Face face, int x, int y){
			this.face = face;
			this.x = x;
			this.y = y;
		}
	}
	
	private final Icon side, top, bottom;
	private final TileMachine machine;
	
	public TabConfiguration(TileMachine machine) {
		super("Configuration", Stacks.circuit, TabColor.RED);
		this.machine = machine;
		side = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE_I);
		top = IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP_I);
		bottom = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM_I);
	}

	@Override
	public int getSizeY() {
		return 80;
	}

	@Override
	public void render() {
		gui.drawDarkerArea(getColor(), 23, 21, 60, 60);
		for(int i = 0; i < 6; i++){
			Side s = sides[i];
			FaceColor color = machine.getColor(s.face);
			if(!color.isColored()){
				gui.drawIcon(s.x, s.y, machine.getBlockType().getIcon(i, 0), TextureMap.locationBlocksTexture, 16);
			}else{
				Icon icon;
				switch(s.face){
				case BOTTOM:
					icon = bottom;
					break;
				case TOP:
					icon = top;
					break;
				default:
					icon = side;
					break;
				}
				gui.drawIcon(s.x, s.y, icon, TextureMap.locationBlocksTexture, 16);
				gui.drawIcon(s.x, s.y, IconRegistry.blockIcons.get(color.getTexture()), TextureMap.locationBlocksTexture, 16);
			}
		}
	}
	
	@Override
	public boolean handleClick(int x, int y){
		for(Side side : sides){
			if(elementHit(side.x, side.y, x, y, 16, 16)){
				gui.playClick();
				PacketToggleMachineFace packet = new PacketToggleMachineFace();
				packet.x = machine.xCoord;
				packet.y = machine.yCoord;
				packet.z = machine.zCoord;
				packet.face = side.face;
				packet.sendToServer();
			}
		}
		return !elementHit(23, 21, x, y, 60, 60);
	}
}
