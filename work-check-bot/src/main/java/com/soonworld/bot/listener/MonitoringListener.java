package com.soonworld.bot.listener;

import com.soonworld.bot.config.AppConfig;
import com.soonworld.bot.user.Users;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class MonitoringListener extends ListenerAdapter {
    public static final String MONITORING_CHANNEL_ID = "1291976100590649359";
    private static final Pattern URL_PATTERN= Pattern.compile("^https://github\\.com/.*/commits?/");

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(MONITORING_CHANNEL_ID.equals(event.getChannel().getId())) {
            if(URL_PATTERN.matcher(event.getMessage().getContentRaw()).find()) {
                final var channel = event.getJDA().getTextChannelById(MONITORING_CHANNEL_ID);
                final var name = event.getAuthor().getName();
                Users.set.add(name);
                Objects.requireNonNull(channel)
                        .sendMessage(AppConfig.userMap.get(name) + "님 과제하느라 수고했어요^^, 진실의 방은 안가셔도 되겠어").queue();
            }
        }
    }
}
