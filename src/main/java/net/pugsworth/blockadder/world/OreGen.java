package net.pugsworth.blockadder.world;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.OreFeatureConfig.Target;
import net.pugsworth.blockadder.BlockadderMod;
import net.pugsworth.blockadder.world.decorator.RandomiteDecorator;
import net.pugsworth.blockadder.world.gen.feature.IGenFunction;
import net.minecraft.world.gen.feature.Feature;

public class OreGen
{
    // public static final Decorator<NopeDecoratorConfig> RANDOMITE_DECORATOR = Registry.register(Registry.DECORATOR, "randomite_decorator", new RandomiteDecorator(NopeDecoratorConfig::deserialize));
    public static final Feature<OreFeatureConfig> RANDOMITE_FEATURE = Registry.register(Registry.FEATURE, "randomite_ore", new OreFeature(OreFeatureConfig::deserialize));

    private static IGenFunction generator;

    public static void init()
    {
        // RegistryEntryAddedCallback(Registry.BIOME).register((i, identifier, biome) -> handleBiome(biome));

        switch (BlockadderMod.CONFIG.generationType)
        {
            case "cross":
                generator = OreGen::genCross;
                break;
            case "single":
                generator = OreGen::genSingle;
                break;
            case "vein":
            default:
                generator = OreGen::genVein;
                break;
        }

        Registry.BIOME.forEach(biome -> handleBiome(biome));
    }

    private static void handleBiome(Biome biome) {
		if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
            generator.gen(biome);
		}
    }

    private static void genCross(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
            Biome.configureFeature(
                RANDOMITE_FEATURE, 
                new OreFeatureConfig(
                    Target.NATURAL_STONE,
                    BlockadderMod.RANDOMITEORE_BLOCK.getDefaultState(),
                    3
                ), 
                Decorator.COUNT_RANGE, 
                new RangeDecoratorConfig(
                    BlockadderMod.CONFIG.veinCount,
                    BlockadderMod.CONFIG.veinBottom,
                    BlockadderMod.CONFIG.veinTop,
                    BlockadderMod.CONFIG.veinMaximum
                )
            )
        );
    }

    private static void genTest(Biome biome)
    {

    }

    public static void genVein(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
            Biome.configureFeature(
                Feature.ORE, 
                new OreFeatureConfig(
                    Target.NATURAL_STONE,
                    BlockadderMod.RANDOMITEORE_BLOCK.getDefaultState(),
                    BlockadderMod.CONFIG.veinSize),
                Decorator.COUNT_RANGE,
                new RangeDecoratorConfig(
                    BlockadderMod.CONFIG.veinCount,
                    BlockadderMod.CONFIG.veinBottom,
                    BlockadderMod.CONFIG.veinTop,
                    BlockadderMod.CONFIG.veinMaximum
                )
            )
        );
    }

    private static void genSingle(Biome biome) 
    {
        biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, 
            Biome.configureFeature(
                Feature.EMERALD_ORE,
                new net.minecraft.world.gen.feature.EmeraldOreFeatureConfig(
                    Blocks.STONE.getDefaultState(),
                    BlockadderMod.RANDOMITEORE_BLOCK.getDefaultState()
                ),
                Decorator.COUNT_RANGE,
                new RangeDecoratorConfig(
                    BlockadderMod.CONFIG.veinCount, // 50
                    BlockadderMod.CONFIG.veinBottom, // 0
                    BlockadderMod.CONFIG.veinTop, // 0
                    BlockadderMod.CONFIG.veinMaximum // 112
                )
            )
        );
    }
}



