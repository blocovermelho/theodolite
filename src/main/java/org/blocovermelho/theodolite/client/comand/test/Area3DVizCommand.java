package org.blocovermelho.theodolite.client.comand.test;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import org.blocovermelho.theodolite.client.TheodoliteClient;
import org.blocovermelho.theodolite.client.render.debug.Area3DRenderer;
import org.blocovermelho.theodolite.client.render.debug.DebugRenderer;
import org.blocovermelho.theodolite.core.octree.OctDirection;
import org.blocovermelho.theodolite.core.octree.OctNode;
import org.blocovermelho.theodolite.core.octree.iter.OctNodeDirectChildAreaIterator;
import org.blocovermelho.theodolite.core.octree.iter.OctTreeNodeIterator;
import org.blocovermelho.theodolite.core.pos.Area3D;
import org.blocovermelho.theodolite.core.pos.Pos3D;
import org.blocovermelho.theodolite.core.pos.Region2I;
import org.blocovermelho.theodolite.core.utils.NumericalConstants;
import org.blocovermelho.theodolite.core.utils.arithmetic.BitShift;
import org.blocovermelho.theodolite.core.utils.render.types.Color4f;

import java.util.Objects;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Area3DVizCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("a3dc")
                .executes(
                    c -> {


                        DebugRenderer.getInstance().unsetBoxes();

                        var source = c.getSource();
                        var pos =  Pos3D.of(Objects.requireNonNull(source.getClient().getCameraEntity()).getPos());

                        ClientPlayerEntity player = source.getPlayer();
                        ChunkPos chunkPos = player.getChunkPos();

                        byte detail = NumericalConstants.CHUNK_DETAIL_LEVEL + 2;

                        Area3I area = new Area3I(Region2I.of(chunkPos));
                        // 1, 2, 4, 8, 16, 32, 64, 128
                        Area3D playerArea = new Area3D(player.getBlockPos());

                        OctNode<String> octNode = new OctNode<>(area, NumericalConstants.BLOCK_DETAIL_LEVEL);
                        octNode.setValue(playerArea, "Data yay!");

                        var iter = new OctTreeNodeIterator<>(octNode, false);


                        player.sendMessage(Text.of("OctNode<>: " + octNode));

                        iter.forEachRemaining(x -> {
                            DebugRenderer.getInstance().addBox(x.sectionPos);
                            player.sendMessage(Text.of("OctNode<>: " + x));
                        });

                        player.sendMessage(Text.of("Area: " + area));
                        player.sendMessage(Text.of("Player Area: " + playerArea));

                        return  1;
                    }
                )
        );
    }
}
