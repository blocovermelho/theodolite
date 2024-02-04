package org.blocovermelho.theodolite.client.render.debug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import org.blocovermelho.theodolite.core.pos.Area3I;
import org.blocovermelho.theodolite.core.utils.render.types.Color4f;

import java.util.HashMap;
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

    public Set<Area3I> boxes = new HashSet<>();
    public HashMap<Area3I, Color4f> colors = new HashMap<>();
    private BufferBuilder buffer;

    static Color4f[] COLORS = new Color4f[] {
            new Color4f(1.f, 0.f, 0.f, 1.f)
    };

    public void renderBoxes(Camera camera) {
        for (Area3I box : boxes) {
            var color = (colors.containsKey(box)) ? colors.get(box) : COLORS[0];
            Area3DRenderer.OpenGL.outline(box, camera.getPos(), color);
        }
    }

    public void unsetBoxes() {
        this.boxes.clear();
    }

    public void addBox(Area3I box) {
        this.boxes.add(box);
    }
    public void addBox(Area3I box, Color4f color) {
        this.boxes.add(box);
        this.colors.put(box, color);
    }
}
