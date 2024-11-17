package me.eldodebug.soar;

import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import me.eldodebug.soar.gui.modmenu.GuiModMenu;
import me.eldodebug.soar.logger.SoarLogger;
import me.eldodebug.soar.management.account.Account;
import me.eldodebug.soar.management.account.AccountManager;
import me.eldodebug.soar.management.account.AccountType;
import me.eldodebug.soar.management.cape.CapeManager;
import me.eldodebug.soar.management.cape.impl.Cape;
import me.eldodebug.soar.management.event.EventTarget;
import me.eldodebug.soar.management.event.impl.EventClickMouse;
import me.eldodebug.soar.management.event.impl.EventJoinServer;
import me.eldodebug.soar.management.event.impl.EventLocationCape;
import me.eldodebug.soar.management.event.impl.EventLocationSkin;
import me.eldodebug.soar.management.event.impl.EventReceivePacket;
import me.eldodebug.soar.management.event.impl.EventTick;
import me.eldodebug.soar.management.event.impl.EventUpdate;
import me.eldodebug.soar.management.profile.Profile;
import me.eldodebug.soar.utils.OptifineUtils;
import me.eldodebug.soar.utils.TargetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.util.ResourceLocation;

public class SoarHandler {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	private final Soar instance;
	
	private String prevOfflineName;
	private ResourceLocation offlineSkin;
	
	public SoarHandler() {
        instance = Soar.getInstance();
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		OptifineUtils.disableFastRender();
	}
	
	@EventTarget
	public void onJoinServer(EventJoinServer event) {
		for(Profile p : instance.getProfileManager().getProfiles()) {
			if(!p.getServerIp().isEmpty() && StringUtils.containsIgnoreCase(event.getIp(), p.getServerIp())) {
				instance.getModManager().disableAll();
				instance.getProfileManager().load(p.getJsonFile());
				break;
			}
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		TargetUtils.onUpdate();
	}
	
	@EventTarget
	public void onClickMouse(EventClickMouse event) {
        if (mc.gameSettings.keyBindTogglePerspective.isPressed()) {
            mc.gameSettings.thirdPersonView = (mc.gameSettings.thirdPersonView + 1) % 3;
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
    	if(event.getPacket() instanceof S2EPacketCloseWindow && mc.currentScreen instanceof GuiModMenu) {
    		event.setCancelled(true);
    	}
	}
	
	@EventTarget
	public void onCape(EventLocationCape event) {
		
		CapeManager capeManager = instance.getCapeManager();
		
		if(event.getPlayerInfo() != null && event.getPlayerInfo().getGameProfile().getId().equals(mc.thePlayer.getGameProfile().getId())) {
			
			Cape currentCape = capeManager.getCurrentCape();
			
			if(!currentCape.equals(capeManager.getCapeByName("None"))) {
				event.setCancelled(true);
				event.setCape(currentCape.getCape());
			}
		}
	}
	
	@EventTarget
	public void onSkin(EventLocationSkin event) {
		
		if(event.getPlayerInfo() != null && event.getPlayerInfo().getGameProfile().getId().equals(mc.thePlayer.getGameProfile().getId())) {
			
			AccountManager accountManager = instance.getAccountManager();
			Account currentAccount = accountManager.getCurrentAccount();
			
			if(currentAccount.getType().equals(AccountType.OFFLINE)) {
				
				if(!Objects.equals(prevOfflineName, currentAccount.getName())) {
					
					if(currentAccount.getSkinFile() != null) {
						try {
							
				            BufferedImage t = ImageIO.read(currentAccount.getSkinFile());
				            DynamicTexture nibt = new DynamicTexture(t);
				            
				            offlineSkin = mc.getTextureManager().getDynamicTextureLocation("offlineskin", nibt);
						} catch(Exception e) {
							SoarLogger.error("Failed to load offline skin", e);
						}
					}
				}
				
				if(offlineSkin != null) {
					event.setCancelled(true);
					event.setSkin(offlineSkin);
				}
			}
		}
	}
}
