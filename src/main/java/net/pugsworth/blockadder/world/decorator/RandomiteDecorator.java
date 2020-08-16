package net.pugsworth.blockadder.world.decorator;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.decorator.NopeDecorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.decorator.SimpleDecorator;

import com.mojang.datafixers.Dynamic;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class RandomiteDecorator extends NopeDecorator {

    private static final Vec3i[] gen_cross = {
        new Vec3i(0, 1, 0),
        new Vec3i(0, -1, 0),
        new Vec3i(1, 0, 0),
        new Vec3i(-1, 0, 0),
        new Vec3i(0, 0, 1),
        new Vec3i(0, 0, -1)
    };

    private static Vec3i[] positions = new Vec3i[5*6];

    public RandomiteDecorator(Function<Dynamic<?>, ? extends NopeDecoratorConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public Stream<BlockPos> getPositions(Random random, NopeDecoratorConfig config, BlockPos blockPos) {
        int x = 0;
        Vec3i cross;

        for (int _i = 0; _i < 5; _i++) {
            int i = 1 + random.nextInt(15);
            int j = 8 + random.nextInt(64);
            int k = 1 + random.nextInt(15);

            for (int _j = 0; _j < gen_cross.length; _j++) {
                cross = gen_cross[_j];
                positions[x++] = new Vec3i(i+cross.getX(), j+cross.getY(), k+cross.getZ());
            }
        }

        return IntStream.range(0, positions.length).mapToObj((idx) -> {
            return blockPos.add(positions[idx]);
        });
    }
}