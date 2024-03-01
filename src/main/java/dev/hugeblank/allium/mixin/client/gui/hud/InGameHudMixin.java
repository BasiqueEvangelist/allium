package dev.hugeblank.allium.mixin.client.gui.hud;

import dev.hugeblank.allium.lua.api.DefaultEventsLib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    private final InGameHud thiz = (InGameHud) (Object) this;

    @Inject(at = @At("HEAD"), method = "render")
    private void renderHead(DrawContext context, float tickDelta, CallbackInfo ci) {
        DefaultEventsLib.CLIENT_RENDER_HEAD.invoker().onGuiRender(client, context, tickDelta, scaledWidth, scaledHeight, thiz.getTextRenderer());
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void renderTail(DrawContext context, float tickDelta, CallbackInfo ci) {
        DefaultEventsLib.CLIENT_RENDER_TAIL.invoker().onGuiRender(client, context, tickDelta, scaledWidth, scaledHeight, thiz.getTextRenderer());
    }
}
