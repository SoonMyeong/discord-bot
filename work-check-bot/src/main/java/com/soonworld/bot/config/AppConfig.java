package com.soonworld.bot.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
public class AppConfig {
    private String token;
    public static Map<String, String> userMap;
    public static long price;

    public AppConfig() {
        try (
                var input = AppConfig.class.getClassLoader().getResourceAsStream("local.properties");
                var reader = new InputStreamReader(input, StandardCharsets.UTF_8)
        ) {
            final var properties = new Properties();
            properties.load(reader);
            token =  properties.getProperty("token");
            userMap = new HashMap<>();
            for(int i = 1; i<=5; i++) {
                userMap.put(properties.getProperty("user.map.key"+i),properties.getProperty("user.map.value"+i));
            }
            price = Long.parseLong(properties.getProperty("price"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
