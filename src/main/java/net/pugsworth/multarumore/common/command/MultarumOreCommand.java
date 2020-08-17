package net.pugsworth.multarumore.common.command;

import java.util.Iterator;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.Vec2ArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;

import static net.minecraft.server.command.CommandManager.literal;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.pugsworth.multarumore.MultarumOreMod;
import net.pugsworth.multarumore.block.MultarumOreBlock;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;


public class MultarumOreCommand
{
    public static final String ROOT_COMMAND = "multiarum";
    public static final String CLEAR_STONE_SUBCOMMAND = "clearstone";

    private static Block[] _findBlocks = {Blocks.STONE, Blocks.ANDESITE, Blocks.GRANITE, Blocks.DIORITE, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.WATER, Blocks.LAVA};

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal(ROOT_COMMAND)
        .requires(s -> s.hasPermissionLevel(2))
        .then(literal(CLEAR_STONE_SUBCOMMAND)
            .then(
                CommandManager.argument("size", Vec2ArgumentType.vec2())
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        Vec3d pos = source.getPosition();
                        World world = source.getWorld();
                        Vec2f argSize = Vec2ArgumentType.getVec2(context, "size");

                        removeBlocks(world, pos, argSize);

                        return 0;
                    })
            )
            .executes(context -> {
                ServerCommandSource source = context.getSource();
                World world = source.getWorld();
                Vec3d pos = source.getPosition();

                removeBlocks(world, pos, new Vec2f(2.0f, 2.0f));

                return 0;
            }));

        dispatcher.register(builder);
    }

    private static void removeBlocks(World world, Vec3d pos, Vec2f size)
    {
        int width = (int)size.x;
        int height = (int)size.y;

        BlockBox box = new BlockBox((int)pos.x - width/2, (int)pos.y-64, (int)pos.z-height/2, (int)pos.x+width/2, (int)pos.y, (int)pos.z+height/2);
        Iterator iter = BlockPos.iterate(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ).iterator();

        while (iter.hasNext())
        {
            BlockPos bpos = (BlockPos)iter.next();
            Block block = world.getBlockState(bpos).getBlock();

            if (!block.equals(MultarumOreMod.MULTARUMORE_BLOCK)) {
                world.setBlockState(bpos, Blocks.AIR.getDefaultState());
                world.updateNeighbors(bpos, block);
            }

            /*
            for (int i = 0; i < _findBlocks.length; i++) {
                // if (_findBlocks[i].equals(block)) {
                    world.setBlockState(bpos, Blocks.AIR.getDefaultState());
                    world.updateNeighbors(bpos, block);
                }
            }
            */

        }
    }
}