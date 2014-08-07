package lordfokas.stargatetech2.enemy.gui;

import java.util.List;

import org.lwjgl.input.Mouse;

import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.core.base.BaseGUI;
import lordfokas.stargatetech2.core.base.BaseGauge.TankGauge;
import lordfokas.stargatetech2.core.machine.tabs.TabAbstractBus;
import lordfokas.stargatetech2.core.machine.tabs.TabConfiguration;
import lordfokas.stargatetech2.core.reference.BlockReference;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.enemy.packet.PacketExceptionsUpdate;
import lordfokas.stargatetech2.enemy.packet.PacketPermissionsUpdate;
import lordfokas.stargatetech2.enemy.tileentity.TileShieldController;
import lordfokas.stargatetech2.integration.plugins.te3.CoFHFriendHelper;

public class GUIShieldController extends BaseGUI {
	private TileShieldController shieldController;
	private TankGauge ionTank;
	private TextHandler textHandler = new TextHandler();
	private ExceptionList exceptions = new ExceptionList();
	private IClickHandler addException, remException, toggleBit;
	
	private class ExceptionList{
		public String[] names = new String[10];
		private int last = 0;
		private int lastMax = -1;
		
		public void lastInc(){
			if(last < lastMax) last++;
		}
		
		public void lastDec(){
			if(last > 0) last--;
		}
		
		public void buildFrom(ShieldPermissions perm){
			names = new String[names.length];
			List<String> ex = perm.getExceptionList();
			lastMax = ex.size() - 1;
			if(last > lastMax) last = lastMax;
			int s = last + 1 - names.length;
			if(s < 0) s = 0;
			for(int i = 0; i < names.length; i++){
				if(s+i > ex.size()-1) break;
				names[i] = ex.get(s+i);
			}
		}
	}
	
	public GUIShieldController(ContainerShieldController container) {
		super(container, 200, 140, true);
		shieldController = container.controller;
		bgImage = TextureReference.GUI_SHIELD_CONTROLLER;
		//addGauge(new TankGauge(5, 75, shieldController.tank));
		addTab(new TabAbstractBus(shieldController));
		addTab(new TabConfiguration(shieldController));
		
		addException = new IClickHandler(){
			@Override
			public void onClick(int x, int y) {
				String name = textHandler.toString();
				if(name != null && !name.isEmpty()){
					PacketExceptionsUpdate update = new PacketExceptionsUpdate();
					update.x = shieldController.xCoord;
					update.y = shieldController.yCoord;
					update.z = shieldController.zCoord;
					update.playerName = name;
					update.isSetting = true;
					update.sendToServer();
					playClick(0.8F);
					textHandler = new TextHandler();
				}
			}
		};
		
		remException = new IClickHandler(){
			@Override
			public void onClick(int x, int y) {
				y -= 46;
				for(int n = 0; n < exceptions.names.length; n++){
					if(y > n*9 && y < (n+1)*9){
						String name = exceptions.names[n];
						if(name != null && !name.isEmpty()){
							PacketExceptionsUpdate update = new PacketExceptionsUpdate();
							update.x = shieldController.xCoord;
							update.y = shieldController.yCoord;
							update.z = shieldController.zCoord;
							update.playerName = name;
							update.isSetting = false;
							update.sendToServer();
							playClick(0.7F);
						}
						return;
					}
				}
			}
		};
		
		toggleBit = new IClickHandler(){
			@Override
			public void onClick(int x, int y) {
				y -= 31;
				for(int n = 0; n < 6; n++){
					int b = n*18;
					if(y > b && y < b+9){
						if(n == 0){
							if(!CoFHFriendHelper.isSystemEnabled()){
								playClick(0.25F);
								return;
							}
						}
						int perm = 1 << n;
						PacketPermissionsUpdate update = new PacketPermissionsUpdate();
						update.x = shieldController.xCoord;
						update.y = shieldController.yCoord;
						update.z = shieldController.zCoord;
						update.permissionFlag = perm;
						update.isSetting = !shieldController.getPermissions().hasBit(perm);
						update.sendToServer();
						playClick(update.isSetting ? 0.8F : 0.7F);
						return;
					}
				}
			}
		};
		
		addClickHandler(addException, 184, 30, 12, 12);
		addClickHandler(remException, 105, 46, 8, 89);
		addClickHandler(toggleBit, 5, 31, 8, 98);
	}
	
	@Override
	protected void drawForeground(){
		bindImage(TextureReference.getTexture("blocks/" + BlockReference.SHIELD_CONTROLLER + ".png"));
		drawQuad(4, 3.5F, 0, 1, 0, 1, 8, 8);
		drawLeft("Shield Controller", 16, 4, 0x444444);
		drawLeft("Entity Perms", 4, 19, 0x444444);
		drawLeft("Player Exceptions", 103, 19, 0x444444);
		drawLeft("CoFH Friends", 16, 32, 0x444444);
		drawLeft("Other Players", 16, 50, 0x444444);
		drawLeft("Villagers", 16, 68, 0x444444);
		drawLeft("Animals", 16, 86, 0x444444);
		drawLeft("Monsters", 16, 104, 0x444444);
		drawLeft("Vessels", 16, 122, 0x444444);
		String input = textHandler.getString(99);
		if(input != null && !input.isEmpty()){
			drawLeft(input, 105, 33, 0xFFFFFF);
		}
		for(int i = 0; i < exceptions.names.length; i++){
			String name = exceptions.names[i];
			if(name == null || name.isEmpty()) break;
			bindBGImage();
			drawLocalQuad(105, 46 + (i * 9), 248, 256, 8, 16, 8, 8);
			drawLeft(name, 114, 46 + (i * 9), 0xFFFFFF);
		}
		bindBGImage();
		ShieldPermissions perm = shieldController.getPermissions();
		exceptions.buildFrom(perm);
		if(CoFHFriendHelper.isSystemEnabled()){
			if(perm.hasBit(ShieldPermissions.PERM_FRIEND)) drawLocalQuad(5, 31, 248, 256, 0, 8, 8, 8);
		}else{
			drawLocalQuad(5, 31, 240, 248, 0, 8, 8, 8);
		}
		if(perm.hasBit(ShieldPermissions.PERM_PLAYER)) drawLocalQuad(5, 49, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_VILLAGER)) drawLocalQuad(5, 67, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_ANIMAL)) drawLocalQuad(5, 85, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_MONSTER)) drawLocalQuad(5, 103, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_VESSEL)) drawLocalQuad(5, 121, 248, 256, 0, 8, 8, 8);
		//if(shieldController.hasColor(FaceColor.BLUE)) drawFrame(FaceColor.BLUE, 3, 73, 20, 68);
	}
	
	@Override
	protected void onKeyTyped(char key, int code){
		if(code == 28){ // Enter
			addException.onClick(-1, -1);
		}else if(code == 200){ // Arrow Up
			exceptions.lastDec();
		}else if(code == 208){ // Arrow Down
			exceptions.lastInc();
		}else{
			// TODO: implement text focus.
			textHandler.onKey(key, code);
		}
	}
	
	@Override
	protected void processMouseEvents(){
		int wheel = Mouse.getDWheel();
		if(wheel < 0){
			exceptions.lastInc();
		}else if(wheel > 0){
			exceptions.lastDec();
		}
	}
}