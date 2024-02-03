package org.blocovermelho.theodolite.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import org.blocovermelho.theodolite.client.render.events.WorldRenderEndCallback;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/LightmapTextureManager;FDDD)V", ordinal = 1), cancellable = true)
    private void onRenderWorldLastNormal(
            MatrixStack matrices,
            float tickDelta, long limitTime, boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightmapTextureManager lightmapTextureManager,
            Matrix4f projMatrix,
            CallbackInfo ci)
    {
        ActionResult result = WorldRenderEndCallback.EVENT.invoker().render(matrices, camera, projMatrix);

        if(result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
