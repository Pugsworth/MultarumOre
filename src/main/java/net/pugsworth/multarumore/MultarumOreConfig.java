package net.pugsworth.multarumore;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import org.apache.logging.log4j.Level;

public class MultarumOreConfig {
    // chance of tnt dropping instead of ore
    public boolean tntEnabled = true;
    public float tntChance = 1.0f/64.0f;

    // how the ore generates in the world
    // choices are cross, single, vein, pillars
    public String generationType = "cross";

    // public byte genYMinimum = 8;
    // public byte genYMaximum = 112;

    // vein settings
    public int veinSize = 5;
    public int veinCount = 8;
    public int veinBottom = 8;
    public int veinTop = 0;
    public int veinMaximum = 32;

    public MultarumOreConfig()
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


    public static String serialize(MultarumOreConfig config)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(config);
    }

    public static MultarumOreConfig deserialize(String json)
    {
        Gson gson = new Gson();
        MultarumOreConfig config;
        try {
            config = gson.fromJson(json, MultarumOreConfig.class);
        } catch (JsonSyntaxException e) {
            MultarumOreMod.logger.log(Level.ERROR, e.getMessage());
            e.printStackTrace();
            return new MultarumOreConfig();
        }

        if (config.tntChance > 1.0f) {
            config.tntChance = config.tntChance / 100.0f; // if value is > 1.0, it is a percentage. Convert to decimal.
        }
        
        return config;
    }

}