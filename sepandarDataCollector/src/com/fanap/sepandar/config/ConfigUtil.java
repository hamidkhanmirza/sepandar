package com.fanap.sepandar.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin123 on 2/13/2019.
 */
public class ConfigUtil {
    static Config config;

    static {
        load(ConfigUtil.class.getResourceAsStream("/config.yaml"));
    }

    public static void load(InputStream inputStream) {
        Yaml yaml = new Yaml();
        config = yaml.loadAs(inputStream, Config.class);
    }

    public static Config getConfig() {
        return config;
    }
}
