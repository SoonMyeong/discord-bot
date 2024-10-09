package com.soonworld.bot.listener;

import com.soonworld.bot.config.AppConfig;
import com.soonworld.bot.user.UserList;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class MonitoringListener extends ListenerAdapter {
    private static final String MONITORING_CHANNEL_ID = "1291976100590649359";
    private static final Pattern URL_PATTERN= Pattern.compile("^https://github\\.com/.*/commit/");
    private final AppConfig config;
    private final UserList userList;

    public MonitoringListener(AppConfig config, UserList userList) {
        this.config = config;
        this.userList = userList;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(MONITORING_CHANNEL_ID.equals(event.getChannel().getId())) {
            if(URL_PATTERN.matcher(event.getMessage().getContentRaw()).find()) {
                final var channel = event.getJDA().getTextChannelById(MONITORING_CHANNEL_ID);
                final var name = event.getAuthor().getGlobalName();
                userList.add(name);
                Objects.requireNonNull(channel).sendMessage(name + "님 과제하느라 수고했어요^^, 진실의 방은 안가셔도 되겠어").queue();
            }
        }
        final var test = event.getMessage().getContentRaw();

        if("숙제검사".equals(event.getMessage().getContentRaw())) {
            report(event);
        }
        if("검사완료".equals(event.getMessage().getContentRaw())) {
            userList.clear();
        }
    }

    private void report(@NotNull MessageReceivedEvent event) {
        final var savedUserList = config.getUserList();
        if(savedUserList.isEmpty()) {
            event.getChannel().sendMessage("모두 숙제를 완료 했습니다.");
        }
        savedUserList.forEach(u -> {
            if(!userList.contains(u)) {
                event.getChannel().sendMessage(u + "님 벌금 1,000원 입금 부탁드립니다.").queue();
            }
        });
    }
}
