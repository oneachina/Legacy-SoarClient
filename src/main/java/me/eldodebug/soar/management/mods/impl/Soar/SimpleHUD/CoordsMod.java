package me.eldodebug.soar.management.mods.impl.Soar.SimpleHUD;

import java.util.ArrayList;
import java.util.Arrays;

import me.eldodebug.soar.Soar;
import me.eldodebug.soar.management.event.EventTarget;
import me.eldodebug.soar.management.event.impl.EventRender2D;
import me.eldodebug.soar.management.language.TranslateText;
import me.eldodebug.soar.management.mods.SimpleHUDMod;
import me.eldodebug.soar.management.mods.settings.impl.BooleanSetting;
import me.eldodebug.soar.management.mods.settings.impl.ComboSetting;
import me.eldodebug.soar.management.mods.settings.impl.combo.Option;
import me.eldodebug.soar.management.nanovg.NanoVGManager;
import me.eldodebug.soar.management.nanovg.font.Fonts;
import me.eldodebug.soar.management.nanovg.font.Icon;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class CoordsMod extends SimpleHUDMod {

	private ComboSetting designSetting = new ComboSetting(TranslateText.DESIGN, this, TranslateText.SIMPLE, new ArrayList<Option>(Arrays.asList(
			new Option(TranslateText.SIMPLE), new Option(TranslateText.FANCY))));
	private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);
	
	public CoordsMod() {
		super(TranslateText.COORDS, TranslateText.COORDS_DEDSCRIPTION);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		Option design = designSetting.getOption();
		NanoVGManager nvg = Soar.getInstance().getNanoVGManager();
		
		if(design.getTranslate().equals(TranslateText.SIMPLE)) {
			this.draw();
		}else {
			nvg.setupAndDraw(() -> drawNanoVG());
		}
	}
	
	private void drawNanoVG() {
		
		String biome = "";
		Chunk chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(mc.thePlayer));
		int maxWidth = 100;
		biome = chunk.getBiome(new BlockPos(mc.thePlayer), this.mc.theWorld.getWorldChunkManager()).biomeName;
		
		if(maxWidth < (this.getTextWidth("Biome: " + biome, 9, Fonts.REGULAR))) {
			maxWidth = (int) (this.getTextWidth("Biome: " + biome, 9, Fonts.REGULAR) + 12);
		}else {
			maxWidth = 107;
		}
		
		this.drawBackground(maxWidth, 48);
		this.drawText("X: " + (int) mc.thePlayer.posX, 5.5F, 5.5F, 9, Fonts.REGULAR);
		this.drawText("Y: " + (int) mc.thePlayer.posY, 5.5F, 15.5F, 9, Fonts.REGULAR);
		this.drawText("Z: " + (int) mc.thePlayer.posZ, 5.5F, 25.5F, 9, Fonts.REGULAR);
		this.drawText("Biome: " + biome, 5.5F, 35.5F, 9, Fonts.REGULAR);
		
		this.setWidth(maxWidth);
		this.setHeight(48);
	}
	
	@Override
	public String getText() {
		return "X: " + (int) mc.thePlayer.posX + " Y: " + (int) mc.thePlayer.posY + " Z: " + (int) mc.thePlayer.posZ;
	}
	
	@Override
	public String getIcon() {
		return iconSetting.isToggled() ? Icon.MAP_PIN : null;
	}
}
