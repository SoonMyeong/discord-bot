package com.soonworld.bot.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

@Getter
public class AppConfig {
    private String token;
    private List<String> userList;

    public AppConfig() {
        try (
                var input = AppConfig.class.getClassLoader().getResourceAsStream("local.properties");
                var reader = new InputStreamReader(input, StandardCharsets.UTF_8)
        ) {
            final var properties = new Properties();
            properties.load(reader);
            token =  properties.getProperty("token");
            final var users = properties.getProperty("users").split(",");
            userList = List.of(users);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
