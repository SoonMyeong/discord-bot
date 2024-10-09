package com.soonworld.bot;


import com.soonworld.bot.config.AppConfig;
import com.soonworld.bot.listener.MonitoringListener;
import com.soonworld.bot.user.UserList;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class WorkCheckBotApplication {
    public static void main(String[] args) throws InterruptedException {
        final var config = new AppConfig();
        final var userList = new UserList();
        final var jda = JDABuilder.createDefault(config.getToken());

        jda.setActivity(Activity.playing("감시"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MonitoringListener(config, userList))
                .build().awaitReady();
    }
}
