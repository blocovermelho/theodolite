package org.blocovermelho.theodolite.client.render.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import org.joml.Matrix4f;

public interface WorldRenderEndCallback {
    Event<WorldRenderEndCallback> EVENT = EventFactory.createArrayBacked(WorldRenderEndCallback.class,
            (listeners) -> (matrices, camera, projMatrix) -> {
                for (WorldRenderEndCallback listener: listeners) {
                    ActionResult result = listener.render(matrices, camera, projMatrix);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult render(MatrixStack matrices, Camera camera, Matrix4f projMatrix);
}
