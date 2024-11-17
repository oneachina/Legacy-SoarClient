package me.eldodebug.soar.injection.mixin.mixins.layer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.eldodebug.soar.management.mods.impl.Soar.Render.OldAnimationsMod;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {

	@Inject(method = "shouldCombineTextures", at = @At("HEAD"), cancellable = true)
	public void oldArmorDamage(CallbackInfoReturnable<Boolean> cir) {
		
		OldAnimationsMod mod = OldAnimationsMod.getInstance();
		
		cir.setReturnValue(mod.isToggled() && mod.getArmorDamageSetting().isToggled());
	}
}
