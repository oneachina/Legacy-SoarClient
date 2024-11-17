package me.eldodebug.soar.management.mods.impl.SimpleHUD;

import me.eldodebug.soar.management.event.EventTarget;
import me.eldodebug.soar.management.event.impl.EventRender2D;
import me.eldodebug.soar.management.language.TranslateText;
import me.eldodebug.soar.management.mods.SimpleHUDMod;
import me.eldodebug.soar.management.mods.settings.impl.BooleanSetting;
import me.eldodebug.soar.management.nanovg.font.Icon;

public class HealthDisplayMod extends SimpleHUDMod {

	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public HealthDisplayMod() {
		super(TranslateText.HEALTH_DISPLAY, TranslateText.HEALTH_DISPLAY_DESCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.draw();
	}
	
	@Override
	public String getText() {
		return (int) mc.thePlayer.getHealth() + " Health";
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? Icon.HEART : null;
	}
}
