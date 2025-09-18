package dev.doctor4t.trainmurdermystery.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.doctor4t.trainmurdermystery.game.TMMGameConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.component.ComponentMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.List;

public class ResetTrainCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:resetTrain")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> resetTrain(context.getSource()))

        );
    }

    record BlockEntityInfo(NbtCompound nbt, ComponentMap components) {
    }

    record BlockInfo(BlockPos pos, BlockState state, @Nullable BlockEntityInfo blockEntityInfo) {
    }

    private static int resetTrain(ServerCommandSource source) throws CommandSyntaxException {
        BlockPos backupMinPos = BlockPos.ofFloored(TMMGameConstants.BACKUP_TRAIN_LOCATION.getMinPos());
        BlockPos backupMaxPos = BlockPos.ofFloored(TMMGameConstants.BACKUP_TRAIN_LOCATION.getMaxPos());
        BlockPos trainMinPos = BlockPos.ofFloored(TMMGameConstants.TRAIN_LOCATION.getMinPos());
        BlockPos trainMaxPos = BlockPos.ofFloored(TMMGameConstants.TRAIN_LOCATION.getMaxPos());

        BlockBox backupTrainBox = BlockBox.create(backupMinPos, backupMaxPos);
        BlockBox trainBox = BlockBox.create(trainMinPos, trainMaxPos);

        ServerWorld serverWorld = source.getWorld();

        if (serverWorld.isRegionLoaded(backupMinPos, backupMaxPos) && serverWorld.isRegionLoaded(trainMinPos, trainMaxPos)) {
            List<BlockInfo> list = Lists.newArrayList();
            List<BlockInfo> list2 = Lists.newArrayList();
            List<BlockInfo> list3 = Lists.newArrayList();
            Deque<BlockPos> deque = Lists.newLinkedList();
            BlockPos blockPos5 = new BlockPos(trainBox.getMinX() - backupTrainBox.getMinX(), trainBox.getMinY() - backupTrainBox.getMinY(), trainBox.getMinZ() - backupTrainBox.getMinZ());

            for (int k = backupTrainBox.getMinZ(); k <= backupTrainBox.getMaxZ(); ++k) {
                for (int l = backupTrainBox.getMinY(); l <= backupTrainBox.getMaxY(); ++l) {
                    for (int m = backupTrainBox.getMinX(); m <= backupTrainBox.getMaxX(); ++m) {
                        BlockPos blockPos6 = new BlockPos(m, l, k);
                        BlockPos blockPos7 = blockPos6.add(blockPos5);
                        CachedBlockPosition cachedBlockPosition = new CachedBlockPosition(serverWorld, blockPos6, false);
                        BlockState blockState = cachedBlockPosition.getBlockState();

                        BlockEntity blockEntity = serverWorld.getBlockEntity(blockPos6);
                        if (blockEntity != null) {
                            BlockEntityInfo blockEntityInfo = new BlockEntityInfo(blockEntity.createComponentlessNbt(source.getRegistryManager()), blockEntity.getComponents());
                            list2.add(new BlockInfo(blockPos7, blockState, blockEntityInfo));
                            deque.addLast(blockPos6);
                        } else if (!blockState.isOpaqueFullCube(serverWorld, blockPos6) && !blockState.isFullCube(serverWorld, blockPos6)) {
                            list3.add(new BlockInfo(blockPos7, blockState, null));
                            deque.addFirst(blockPos6);
                        } else {
                            list.add(new BlockInfo(blockPos7, blockState, null));
                            deque.addLast(blockPos6);
                        }
                    }
                }
            }

            List<BlockInfo> list4 = Lists.newArrayList();
            list4.addAll(list);
            list4.addAll(list2);
            list4.addAll(list3);
            List<BlockInfo> list5 = Lists.reverse(list4);

            for (BlockInfo blockInfo : list5) {
                BlockEntity blockEntity3 = serverWorld.getBlockEntity(blockInfo.pos);
                Clearable.clear(blockEntity3);
                serverWorld.setBlockState(blockInfo.pos, Blocks.BARRIER.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
            }

            int mx = 0;

            for (BlockInfo blockInfo2 : list4) {
                if (serverWorld.setBlockState(blockInfo2.pos, blockInfo2.state, Block.NOTIFY_LISTENERS | Block.FORCE_STATE)) {
                    ++mx;
                }
            }

            for (BlockInfo blockInfo2 : list2) {
                BlockEntity blockEntity4 = serverWorld.getBlockEntity(blockInfo2.pos);
                if (blockInfo2.blockEntityInfo != null && blockEntity4 != null) {
                    blockEntity4.readComponentlessNbt(blockInfo2.blockEntityInfo.nbt, serverWorld.getRegistryManager());
                    blockEntity4.setComponents(blockInfo2.blockEntityInfo.components);
                    blockEntity4.markDirty();
                }

                serverWorld.setBlockState(blockInfo2.pos, blockInfo2.state, Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
            }

            for (BlockInfo blockInfo2 : list5) {
                serverWorld.updateNeighbors(blockInfo2.pos, blockInfo2.state.getBlock());
            }

            serverWorld.getBlockTickScheduler().scheduleTicks(serverWorld.getBlockTickScheduler(), backupTrainBox, blockPos5);
            if (mx == 0) {
                throw new SimpleCommandExceptionType(Text.translatable("commands.clone.failed")).create();
            } else {
                int n = mx;
                source.sendFeedback(() -> Text.translatable("commands.clone.success", n), true);
                return mx;
            }
        } else {
            throw BlockPosArgumentType.UNLOADED_EXCEPTION.create();
        }
    }
}
