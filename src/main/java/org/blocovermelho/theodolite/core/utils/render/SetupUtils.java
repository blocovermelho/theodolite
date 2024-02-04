package org.blocovermelho.theodolite.core.utils.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Vec3d;
import org.blocovermelho.theodolite.core.pos.Area3I;
import org.blocovermelho.theodolite.core.pos.Area3D;
import org.blocovermelho.theodolite.core.pos.Pos3D;
import org.blocovermelho.theodolite.core.utils.render.types.Color4f;
import org.blocovermelho.theodolite.core.utils.render.types.LineBuilder;
import org.blocovermelho.theodolite.core.utils.render.types.LinkedLineBuilder;

import java.awt.geom.Area;

// https://github.com/maruohon/litematica/blob/pre-rewrite/fabric/1.20.x/src/main/java/fi/dy/masa/litematica/render/RenderUtils.java
public class SetupUtils {
    public static void setupBlend() {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
    }

    public static void setupBlendSimple() {
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
    }

    public static void color(float r, float g, float b, float a) {
        RenderSystem.setShaderColor(r, g, b, a);
    }

    public static void startDrawingLines(BufferBuilder buffer) {
        RenderSystem.lineWidth(1.0f);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.applyModelViewMatrix();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
    }

    public static Area3D cameraTransform(Area3I box, Vec3d cameraPos) {
        double minX = box.getMinCornerPos().getX() - cameraPos.getX();
        double minY = box.getMinCornerPos().getY() - cameraPos.getY();
        double minZ = box.getMinCornerPos().getZ() - cameraPos.getZ();

        double maxX = box.getMaxCornerPos().getX() - cameraPos.getX() + 1;
        double maxY = box.getMaxCornerPos().getY() - cameraPos.getY() + 1;
        double maxZ = box.getMaxCornerPos().getZ() - cameraPos.getZ() + 1;

        return new Area3D(new Pos3D(minX, minY, minZ), new Pos3D(maxX, maxY, maxZ));
    }

    public static void batched_drawBoxOutlines(Area3I box, Vec3d cameraPos, Color4f color, BufferBuilder buffer) {
        var lline = new LinkedLineBuilder(buffer, color);
        var line = new LineBuilder(buffer,color);

        var newBox = cameraTransform(box, cameraPos);

        var min = newBox.getMinCornerPos();
        var max = newBox.getMaxCornerPos();


        // West Side
        lline.andThen(min.getX(), min.getY(), min.getZ())
            .andThen(min.getX(), min.getY(), max.getZ())
            .andThen(min.getX(), max.getY(), max.getZ())
            .andThen(min.getX(), max.getY(), min.getZ())
            .andThen(min.getX(), min.getY(), min.getZ())
            .batch();

        // East Side
        lline.andThen(max.getX(), min.getY(), max.getZ())
            .andThen(max.getX(), min.getY(),  min.getZ())
            .andThen(max.getX(), max.getY(),  min.getZ())
            .andThen(max.getX(), max.getY(),  max.getZ())
            .andThen(max.getX(), min.getY(),  max.getZ())
            .batch();
        ;

        // North side (don't repeat the vertical lines that are done by the east/west sides)
        line.from(max.getX(), min.getY(), min.getZ())
                .to(min.getX(), min.getY(), min.getZ())
                .batch();

        line.from(min.getX(), max.getY(), min.getZ())
                .to(max.getX(), max.getY(), min.getZ())
                .batch();
        // South side (don't repeat the vertical lines that are done by the east/west sides)
        line.from(min.getX(), min.getY(), max.getZ())
                .to(max.getX(), min.getY(), max.getZ())
                .batch();

        line.from(max.getX(), max.getY(), max.getZ())
                .to(min.getX(), max.getY(), max.getZ())
                .batch();
    }

}
