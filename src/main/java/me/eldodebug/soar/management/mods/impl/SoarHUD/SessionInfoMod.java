package me.eldodebug.soar.management.mods.impl.SoarHUD;

import java.util.Arrays;

import me.eldodebug.soar.Soar;
import me.eldodebug.soar.management.event.EventTarget;
import me.eldodebug.soar.management.event.impl.EventJoinServer;
import me.eldodebug.soar.management.event.impl.EventReceivePacket;
import me.eldodebug.soar.management.event.impl.EventRender2D;
import me.eldodebug.soar.management.language.TranslateText;
import me.eldodebug.soar.management.mods.HUDMod;
import me.eldodebug.soar.management.nanovg.NanoVGManager;
import me.eldodebug.soar.management.nanovg.font.Fonts;
import me.eldodebug.soar.management.nanovg.font.Icon;
import me.eldodebug.soar.utils.ServerUtils;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;

public class SessionInfoMod extends HUDMod {

	private int killCount;
	private String[] killTrigger = {"by *", "para *", "fue destrozado a manos de *"};
	
	private long startTime;
	
	public SessionInfoMod() {
		super(TranslateText.SESSION_INFO, TranslateText.SESSION_INFO_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		NanoVGManager nvg = Soar.getInstance().getNanoVGManager();
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		String time;
		
		if(mc.isSingleplayer()) {
			time = "Singleplayer";
		}else {
			long durationInMillis = System.currentTimeMillis() - startTime;
            long second = (durationInMillis / 1000) % 60;
            long minute = (durationInMillis / (1000 * 60)) % 60;
            long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
            time = String.format("%02d:%02d:%02d", hour, minute, second);
		}
		
		this.drawBackground(140, 64);
		this.drawText("Session Info", 5.5F, 6F, 10.5F, Fonts.REGULAR);
		this.drawRect(0, 17.5F, 140, 1);
		
		this.drawText(Icon.CLOCK, 5.5F, 22.5F, 10F, Fonts.ICON);
		this.drawText(time, 18, 24, 9, Fonts.REGULAR);
		
		this.drawText(Icon.SERVER, 5.5F, 22.5F + 13, 10F, Fonts.ICON);
		this.drawText(ServerUtils.getServerIP(), 18, 24 + 12, 9, Fonts.REGULAR);
		
		this.drawText(Icon.USER, 5.5F, 22.5F + 26, 10F, Fonts.ICON);
		this.drawText(killCount + " kill", 18, 24 + 26.5F, 9, Fonts.REGULAR);
		
		this.setWidth(140);
		this.setHeight(64);
	}
	
	@EventTarget
	public void onJoinServer(EventJoinServer event) {
		startTime = System.currentTimeMillis();
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		
        if (ServerUtils.isHypixel() && event.getPacket() instanceof S02PacketChat) {
        	
            S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
            String chatMessage = chatPacket.getChatComponent().getUnformattedText();
	
            String message = StringUtils.stripControlCodes(chatMessage);
            
            if (!message.contains(":") && Arrays.stream(killTrigger).anyMatch(message.replace(mc.thePlayer.getName(), "*")::contains)) {
                killCount++;
            }
        }
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		startTime = System.currentTimeMillis();
	}
}
