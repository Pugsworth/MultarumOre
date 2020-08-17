package net.pugsworth.multarumore.world;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.OreFeatureConfig.Target;
import net.pugsworth.multarumore.MultarumOreMod;
import net.pugsworth.multarumore.world.gen.feature.IGenFunction;
import net.pugsworth.multarumore.world.gen.feature.OreCrossFeature;

public class OreGen
{
    // public static final Decorator<NopeDecoratorConfig> MULTARUM_DECORATOR = Registry.register(Registry.DECORATOR, "multarum_decorator", new MultarumDecorator(NopeDecoratorConfig::deserialize));
    public static final Feature<OreFeatureConfig> MULTARUM_FEATURE = Registry.register(Registry.FEATURE, "multarumore", new OreCrossFeature(OreFeatureConfig::deserialize));

    private static IGenFunction generator;

    public static void init()
    {
        // RegistryEntryAddedCallback(Registry.BIOME).register((i, identifier, biome) -> handleBiome(biome));

        switch (MultarumOreMod.CONFIG.generationType)
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
                MULTARUM_FEATURE, 
                new OreFeatureConfig(
                    Target.NATURAL_STONE,
                    MultarumOreMod.MULTARUMORE_BLOCK.getDefaultState(),
                    1
                ), 
                Decorator.COUNT_RANGE, 
                new RangeDecoratorConfig(
                    MultarumOreMod.CONFIG.veinCount,
                    MultarumOreMod.CONFIG.veinBottom,
                    MultarumOreMod.CONFIG.veinTop,
                    MultarumOreMod.CONFIG.veinMaximum
                )
            )
        );
    }

    public static void genVein(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
            Biome.configureFeature(
                Feature.ORE, 
                new OreFeatureConfig(
                    Target.NATURAL_STONE,
                    MultarumOreMod.MULTARUMORE_BLOCK.getDefaultState(),
                    MultarumOreMod.CONFIG.veinSize),
                Decorator.COUNT_RANGE,
                new RangeDecoratorConfig(
                    MultarumOreMod.CONFIG.veinCount,
                    MultarumOreMod.CONFIG.veinBottom,
                    MultarumOreMod.CONFIG.veinTop,
                    MultarumOreMod.CONFIG.veinMaximum
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
                    MultarumOreMod.MULTARUMORE_BLOCK.getDefaultState()
                ),
                Decorator.COUNT_RANGE,
                new RangeDecoratorConfig(
                    MultarumOreMod.CONFIG.veinCount, // 50
                    MultarumOreMod.CONFIG.veinBottom, // 0
                    MultarumOreMod.CONFIG.veinTop, // 0
                    MultarumOreMod.CONFIG.veinMaximum // 112
                )
            )
        );
    }
}



