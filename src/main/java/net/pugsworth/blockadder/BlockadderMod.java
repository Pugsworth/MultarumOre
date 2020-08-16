package net.pugsworth.blockadder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pugsworth.blockadder.block.RandomiteOreBlock;
import net.pugsworth.blockadder.common.command.BlockadderCommand;
import net.pugsworth.blockadder.world.OreGen;

public class BlockadderMod implements ModInitializer {

	public static final String MODID = "RandomiteOre";

	public static Path CONFIG_PATH;

	public static final Block RANDOMITEORE_BLOCK = new RandomiteOreBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
	public static final BlockItem EXAMPLE_ITEM = new BlockItem(RANDOMITEORE_BLOCK, new Item.Settings().group(ItemGroup.MISC));

	public static RandomiteConfig CONFIG;

	@Override
	public void onInitialize()
	{
		CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(BlockadderMod.MODID + "/config.json").normalize();

		if (!CONFIG_PATH.toFile().exists())
		{
			saveConfig();
		}
		else
		{
			loadConfig();
		}

		Registry.register(Registry.BLOCK, new Identifier("blockadder", "example_block"), RANDOMITEORE_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("blockadder", "example_block"), EXAMPLE_ITEM);

		OreGen.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			BlockadderCommand.register(dispatcher);
		});
	}

	public boolean loadConfig()
	{
		boolean success = false;
		String config;
		try {
			config = FileUtils.readFileToString(CONFIG_PATH.toFile(), Charset.defaultCharset());
			CONFIG = RandomiteConfig.deserialize(config);

			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("loaded config:");
		System.out.println(CONFIG.tntChance);
		System.out.println(CONFIG.generationType);
		System.out.println(CONFIG.veinSize);
		System.out.println(CONFIG.veinCount);
		System.out.println(CONFIG.veinBottom);
		System.out.println(CONFIG.veinTop);
		System.out.println(CONFIG.veinMaximum);

		return success;
	}

	public boolean saveConfig()
	{
		boolean success = false;
		try {
			CONFIG_PATH.getParent().toFile().mkdirs();

			String config = RandomiteConfig.serialize(CONFIG);
			FileWriter writer = new FileWriter(CONFIG_PATH.toFile());
			writer.write(config);
			writer.close();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}

}

/*
 * Ideas
 * 
 */

/*
 * TODO Config Replace all ores with Randomite Fix oregen refactor names
 */