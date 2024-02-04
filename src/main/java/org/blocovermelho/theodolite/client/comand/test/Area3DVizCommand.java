package org.blocovermelho.theodolite.client.comand.test;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import org.blocovermelho.theodolite.client.render.debug.DebugRenderer;
import org.blocovermelho.theodolite.core.octree.OctNode;
import org.blocovermelho.theodolite.core.octree.iter.OctTreeNodeIterator;
import org.blocovermelho.theodolite.core.pos.Area3I;
import org.blocovermelho.theodolite.core.pos.Pos3I;
import org.blocovermelho.theodolite.core.pos.Region2I;
import org.blocovermelho.theodolite.core.utils.NumericalConstants;
import org.blocovermelho.theodolite.core.utils.render.types.Color4f;

import static dev.xpple.clientarguments.arguments.CBlockPosArgumentType.blockPos;
import static dev.xpple.clientarguments.arguments.CBlockPosArgumentType.getCBlockPos;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Area3DVizCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("a3dc")
                .then(
                        argument("pos1", blockPos()).then(
                                argument("pos2", blockPos())
                                        .executes(ctx -> {
                                            DebugRenderer.getInstance().unsetBoxes();
                                            var source = ctx.getSource();

                                            ClientPlayerEntity player = source.getPlayer();
                                            ChunkPos chunkPos = player.getChunkPos();

                                            Pos3I pos1 = Pos3I.of(getCBlockPos(ctx, "pos1"));
                                            Pos3I pos2 = Pos3I.of(getCBlockPos(ctx, "pos2"));

                                            Area3I zoneArea = new Area3I(pos1, pos2);

                                            Region2I region = Region2I.of(chunkPos);

                                            OctNode<String> octree = OctNode.of(region);


                                            octree.setValue(zoneArea, "value");

                                            player.sendMessage(Text.of("Octree: " + octree));
                                            player.sendMessage(Text.of("Zone Area:" + zoneArea));

                                            var iter = new OctTreeNodeIterator<>(octree, false);

                                            DebugRenderer.getInstance().addBox(zoneArea, new Color4f(0.f, 1.f, 0.f,  1.f));

                                            iter.forEachRemaining(x -> {
                                                if (x.value != null) {
                                                    DebugRenderer.getInstance().addBox(x.sectionPos, new Color4f(0.f, 0.f,  1.f, 1.0f));
                                                } else {
                                                    DebugRenderer.getInstance().addBox(x.sectionPos, new Color4f(1.f, 0.f,  0.f, 0.1f));
                                                }

                                            });

                                            return 1;
                                        })
                        ))
                .executes(
                    c -> {
                        DebugRenderer.getInstance().unsetBoxes();

                        var source = c.getSource();

                        ClientPlayerEntity player = source.getPlayer();
                        ChunkPos chunkPos = player.getChunkPos();

                        byte detail = NumericalConstants.CHUNK_DETAIL_LEVEL + 2;

                        Area3I area = new Area3I(Region2I.of(chunkPos));
                        // 1, 2, 4, 8, 16, 32, 64, 128
                        Area3I playerArea = new Area3I(player.getBlockPos());

                        OctNode<String> octNode  = OctNode.of(Region2I.of(chunkPos));
                        octNode.setValue(playerArea, "Data yay!");

                        var iter = new OctTreeNodeIterator<>(octNode, false);


                        player.sendMessage(Text.of("OctNode<>: " + octNode));

                        iter.forEachRemaining(x -> {
                            DebugRenderer.getInstance().addBox(x.sectionPos);
                            player.sendMessage(Text.of("OctNode<>: " + x));
                        });

                        DebugRenderer.getInstance().addBox(playerArea, new Color4f(0.f, 1.f,  0.f, 1.f));
                        player.sendMessage(Text.of("Area: " + area));
                        player.sendMessage(Text.of("Player Area: " + playerArea));

                        return  1;
                    }
                )
        );
    }
}
