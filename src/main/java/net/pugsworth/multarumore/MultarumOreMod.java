package net.pugsworth.multarumore;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pugsworth.multarumore.block.MultarumOreBlock;
import net.pugsworth.multarumore.common.command.MultarumOreCommand;
import net.pugsworth.multarumore.world.OreGen;

public class MultarumOreMod implements ModInitializer {

	public static final String MODID = "multarumore";

	public static Path CONFIG_PATH;
	public static MultarumOreConfig CONFIG;

	public static Logger logger = LogManager.getLogger(MODID);

	public static final Block MULTARUMORE_BLOCK = registerBlock(new MultarumOreBlock(FabricBlockSettings
		.of(Material.STONE)
		.strength(5.0f, 5.0f)
		.lightLevel(9)
		.breakByTool(FabricToolTags.PICKAXES, 2)
	), "multarum_ore");

	@Override
	public void onInitialize() {
		CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(MultarumOreMod.MODID + ".json").normalize();

		if (!CONFIG_PATH.toFile().exists())
		{
			CONFIG = new MultarumOreConfig();
			saveConfig();
		}
		else
		{
			loadConfig();
			saveConfig(); // save it after loading to update any missing fields
		}

		OreGen.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			MultarumOreCommand.register(dispatcher);
		});
	}

	private static Block registerBlock(Block block, String id)
	{
		Block theblock = Registry.register(Registry.BLOCK, new Identifier(MODID, id), block);
		Registry.register(Registry.ITEM, new Identifier(MODID, id), new BlockItem(theblock, new Item.Settings().group(ItemGroup.MISC)));
		return theblock;
	}

	public boolean loadConfig()
	{
		boolean success = false;
		String config;
		try {
			config = FileUtils.readFileToString(CONFIG_PATH.toFile(), Charset.defaultCharset());
			CONFIG = MultarumOreConfig.deserialize(config);

			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		logger.log(Level.INFO, "loaded config:");
		logger.log(Level.INFO, CONFIG.tntChance);
		logger.log(Level.INFO, CONFIG.generationType);
		logger.log(Level.INFO, CONFIG.veinSize);
		logger.log(Level.INFO, CONFIG.veinCount);
		logger.log(Level.INFO, CONFIG.veinBottom);
		logger.log(Level.INFO, CONFIG.veinTop);
		logger.log(Level.INFO, CONFIG.veinMaximum);
		*/

		return success;
	}

	public boolean saveConfig()
	{
		boolean success = false;
		try {
			// CONFIG_PATH.getParent().toFile().mkdirs();

			String config = MultarumOreConfig.serialize(CONFIG);
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
 * TODO
 * Replace all ores with Multarum
 * Create "Spike" generation
 * Refactor name to permanent name: MultarumOre
 */