package org.blocovermelho.theodolite.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.impl.client.rendering.v0.RenderingCallbackInvoker;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.ActionResult;
import org.blocovermelho.theodolite.client.comand.test.Area3DVizCommand;
import org.blocovermelho.theodolite.client.render.debug.DebugRenderer;
import org.blocovermelho.theodolite.client.render.events.WorldRenderEndCallback;

public class TheodoliteClient implements ClientModInitializer {
    public static boolean DEBUG_STATE = false;

    @Override
    public void onInitializeClient() {

        ClientCommandRegistrationCallback.EVENT.register((l, p) -> {
            Area3DVizCommand.register(l);
        });

        WorldRenderEndCallback.EVENT.register((matrixStack, camera, matrix4f) -> {
            DebugRenderer.getInstance().renderBoxes(camera);
            return ActionResult.PASS;
        });
    }
}
