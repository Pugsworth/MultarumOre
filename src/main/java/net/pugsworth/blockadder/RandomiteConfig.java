package net.pugsworth.blockadder;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

public class RandomiteConfig {
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

    public RandomiteConfig()
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


    public static String serialize(RandomiteConfig config)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(config);
    }

    public static RandomiteConfig deserialize(String json)
    {
        Gson gson = new Gson();
        RandomiteConfig config;
        try {
            config = gson.fromJson(json, RandomiteConfig.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

        if (config.tntChance > 1.0f) {
            config.tntChance = config.tntChance / 100.0f; // if value is > 1.0, it is a percentage. Convert to decimal.
        }
        
        return config;
    }

}