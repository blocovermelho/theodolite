package org.blocovermelho.theodolite.client.render.debug;


import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.blocovermelho.theodolite.core.octree.OctDirection;
import org.blocovermelho.theodolite.core.pos.Area3I;
import org.blocovermelho.theodolite.core.pos.Pos3I;
import org.blocovermelho.theodolite.core.utils.render.SetupUtils;
import org.blocovermelho.theodolite.core.utils.render.types.Color4f;

public class Area3DRenderer {
    public static class OpenGL {
       public static void outline(Area3I area3D, Vec3d cameraPos, Color4f color) {
           // System.out.println("Attempting to render: " + area3D);
           Tessellator tess = Tessellator.getInstance();
           BufferBuilder buffer = tess.getBuffer();

           SetupUtils.setupBlend();
           SetupUtils.startDrawingLines(buffer);

           SetupUtils.batched_drawBoxOutlines(area3D, cameraPos, color, buffer);
           tess.draw();

           RenderSystem.disableBlend();
           // System.out.println("Finished" + area3D);
       }
    }

    public static class ParticleBased {

        public static void fill(Area3I area3I, ClientPlayerEntity playerEntity, BlockState blockState) {
            var world = playerEntity.getWorld();
            for (int x = area3I.getMinCornerPos().getX(); x <= area3I.getMaxCornerPos().getX(); x++) {
                for (int y = area3I.getMinCornerPos().getY(); y <= area3I.getMaxCornerPos().getY(); y++) {
                    for (int z = area3I.getMinCornerPos().getZ(); z <= area3I.getMaxCornerPos().getZ(); z++) {
                        world.setBlockState(new BlockPos(x,y,z), blockState);
                    }
                }
            }

        }
        public static void render(Area3I area3I, ClientPlayerEntity playerEntity, OctDirection direction) {
            var world = playerEntity.getWorld();
            for (int x = area3I.getMinCornerPos().getX(); x <= area3I.getMaxCornerPos().getX(); x++) {
                renderBlock(new Pos3I(x, area3I.getMinCornerPos().getY(), area3I.getMinCornerPos().getZ()), world, direction);
                renderBlock(new Pos3I(x, area3I.getMaxCornerPos().getY(), area3I.getMaxCornerPos().getZ()), world, direction);
                renderBlock(new Pos3I(x, area3I.getMinCornerPos().getY(), area3I.getMaxCornerPos().getZ()), world, direction);
                renderBlock(new Pos3I(x, area3I.getMaxCornerPos().getY(), area3I.getMinCornerPos().getZ()), world, direction);
            }

            for (int y = area3I.getMinCornerPos().getY(); y <= area3I.getMaxCornerPos().getY(); y++) {
                renderBlock(new Pos3I(area3I.getMinCornerPos().getX(), y, area3I.getMinCornerPos().getZ()), world, direction);
                renderBlock(new Pos3I(area3I.getMaxCornerPos().getX(), y, area3I.getMaxCornerPos().getZ()), world, direction);
                renderBlock(new Pos3I(area3I.getMinCornerPos().getX(), y, area3I.getMaxCornerPos().getZ()), world, direction);
                renderBlock(new Pos3I(area3I.getMaxCornerPos().getX(), y, area3I.getMinCornerPos().getZ()), world, direction);
            }

            for (int z = area3I.getMinCornerPos().getZ(); z <= area3I.getMaxCornerPos().getZ(); z++) {
                renderBlock(new Pos3I(area3I.getMinCornerPos().getX(), area3I.getMinCornerPos().getY(), z), world, direction);
                renderBlock(new Pos3I(area3I.getMaxCornerPos().getX(), area3I.getMaxCornerPos().getY(), z), world, direction);
                renderBlock(new Pos3I(area3I.getMinCornerPos().getX(), area3I.getMaxCornerPos().getY(), z), world, direction);
                renderBlock(new Pos3I(area3I.getMaxCornerPos().getX(), area3I.getMinCornerPos().getY(), z), world, direction);
            }
        }
        private static void renderBlock(Pos3I pos, World world, OctDirection direction) {
            world.addParticle(new DustParticleEffect(Vec3d.unpackRgb(direction.asColor()).toVector3f(), 1.0f), pos.getX(), pos.getY(), pos.getZ(), 0,0,0);
            // world.setBlockState(pos.toBlockPos(), direction.asState());
        }
    }
}
