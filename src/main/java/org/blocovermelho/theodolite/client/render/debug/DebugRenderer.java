package org.blocovermelho.theodolite.client.render.debug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import org.blocovermelho.theodolite.core.pos.Area3D;
import org.blocovermelho.theodolite.core.pos.Pos3D;
import org.blocovermelho.theodolite.core.utils.render.types.Color4f;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DebugRenderer {
    private static final DebugRenderer INSTANCE = new DebugRenderer();
    private MinecraftClient mc;

    private DebugRenderer() {
        mc = MinecraftClient.getInstance();
        buffer = Tessellator.getInstance().getBuffer();
    }
    public static DebugRenderer getInstance() { return  INSTANCE; }

    public Set<Area3D> boxes = new HashSet<>();
    private BufferBuilder buffer;

    static Color4f[] COLORS = new Color4f[] {
            new Color4f(1.f, 0.f, 0.f, 1.f)
    };

    public void renderBoxes(Camera camera) {
        for (Area3D box : boxes) {
            Area3DRenderer.OpenGL.outline(box, Pos3D.of(camera.getPos()), COLORS[0]);
        }
    }

    public void unsetBoxes() {
        this.boxes.clear();
    }

    public void addBox(Area3D box) {
        this.boxes.add(box);
    }
}
