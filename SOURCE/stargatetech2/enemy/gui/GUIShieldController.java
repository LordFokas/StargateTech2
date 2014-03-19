package stargatetech2.enemy.gui;

import java.util.List;

import stargatetech2.api.shields.ShieldPermissions;
import stargatetech2.core.base.BaseGUI;
import stargatetech2.core.base.BaseGauge.TankGauge;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.tabs.TabAbstractBus;
import stargatetech2.core.machine.tabs.TabConfiguration;
import stargatetech2.core.reference.BlockReference;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.enemy.tileentity.TileShieldController;

public class GUIShieldController extends BaseGUI {
	private TileShieldController shieldController;
	private TankGauge ionTank;
	private TextHandler textHandler = new TextHandler();
	private ExceptionList exceptions = new ExceptionList();
	
	private class ExceptionList{
		public String[] names = new String[5];
		private int last = 0;
		private int lastMax = -1;
		
		public void lastInc(){
			if(last < lastMax) last++;
		}
		
		public void lastDec(){
			if(last > 0) last--;
		}
		
		public void buildFrom(ShieldPermissions perm){
			names = new String[5];
			List<String> ex = perm.getExceptionList();
			lastMax = ex.size() - 1;
			if(last > lastMax) last = lastMax;
			int s = last - 4;
			if(s < 0) s = 0;
			for(int i = 0; i < 5; i++){
				if(s+i > ex.size()-1) break;
				names[i] = ex.get(s+i);
			}
		}
	}
	
	public GUIShieldController(ContainerShieldController container) {
		super(container, 200, 144, true);
		shieldController = container.controller;
		bgImage = TextureReference.GUI_SHIELD_EMITTER;
		addGauge(new TankGauge(5, 75, shieldController.tank));
		addTab(new TabAbstractBus());
		addTab(new TabConfiguration(shieldController));
	}
	
	@Override
	protected void drawForeground(){
		bindImage(TextureReference.getTexture("blocks/" + BlockReference.SHIELD_CONTROLLER + ".png"));
		drawQuad(4, 3.5F, 0, 1, 0, 1, 8, 8);
		drawLeft("Shield Controller", 16, 4, 0x444444);
		drawCentered("Permissions", 100, 20, 0x444444);
		drawRight("CoFH Friends", 86, 34, 0x444444);
		drawLeft("Other Players", 115, 34, 0x444444);
		drawRight("Villagers", 86, 48, 0x444444);
		drawLeft("Monsters", 115, 48, 0x444444);
		drawRight("Animals", 86, 62, 0x444444);
		drawLeft("Vessels", 115, 62, 0x444444);
		bindBGImage();
		ShieldPermissions perm = shieldController.getPermissions();
		if(perm.hasBit(ShieldPermissions.PERM_FRIEND)) drawLocalQuad(89, 33, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_PLAYER)) drawLocalQuad(104, 33, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_VILLAGER)) drawLocalQuad(89, 47, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_MONSTER)) drawLocalQuad(104, 47, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_ANIMAL)) drawLocalQuad(89, 61, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_VESSEL)) drawLocalQuad(104, 61, 248, 256, 0, 8, 8, 8);
		if(shieldController.hasColor(FaceColor.BLUE)) drawFrame(FaceColor.BLUE, 3, 73, 20, 68);
	}
	
	/*
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
	*/
}