package net.pugsworth.randomiteore;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import org.apache.logging.log4j.Level;

public class RandomiteOreConfig {
    // chance of tnt dropping instead of ore
    public boolean tntEnabled = true;
    public float tntChance = 1.0f/64.0f;

    // how the ore generates in the world
    // choices are cross, single, vein, pillars
    public String generationType = "cross";

    public byte genYMinimum = 8;
    public byte genYMaximum = 112;

    // vein settings
    public int veinSize = 5;
    public int veinCount = 10;
    public int veinBottom = 0;
    public int veinTop = 0;
    public int veinMaximum = 64;

    public RandomiteOreConfig()
    {
        // JsonSerializer<Float> serializer = new JsonSerializer<Float>() {
        //     @Override
        //     public JsonElement serialize(Float src, Type typeOfSrc, JsonSerializationContext context)
        //     {
        //         JsonObject jo = (JsonObject)json;
        //         jo.get()
        //     }
        // };
    }


    public static String serialize(RandomiteOreConfig config)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(config);
    }

    public static RandomiteOreConfig deserialize(String json)
    {
        Gson gson = new Gson();
        RandomiteOreConfig config;
        try {
            config = gson.fromJson(json, RandomiteOreConfig.class);
        } catch (JsonSyntaxException e) {
            RandomiteOreMod.logger.log(Level.ERROR, e.getMessage());
            e.printStackTrace();
            return new RandomiteOreConfig();
        }

        if (config.tntChance > 1.0f) {
            config.tntChance = config.tntChance / 100.0f; // if value is > 1.0, it is a percentage. Convert to decimal.
        }
        
        return config;
    }

}