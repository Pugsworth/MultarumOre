package net.pugsworth.randomiteore.world.gen.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class OreCrossFeature extends Feature<OreFeatureConfig>
{
    public OreCrossFeature(Function<Dynamic<?>, ? extends OreFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    private static final Vec3i[] gen_cross = {
        new Vec3i(0, 1, 0),
        new Vec3i(0, -1, 0),
        new Vec3i(1, 0, 0),
        new Vec3i(-1, 0, 0),
        new Vec3i(0, 0, 1),
        new Vec3i(0, 0, -1)
    };

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, OreFeatureConfig config)
    {
        Vec3i cross;
        BlockPos bp;

        for (int i = 0; i < gen_cross.length; i++) {
            cross = gen_cross[i];
            bp = pos.add(cross.getX(), cross.getY(), cross.getZ());

            if (config.target.getCondition().test(world.getBlockState(bp))) {
                world.setBlockState(bp, config.state, 2);
            }
        }

        return true;
    }
    

    
}