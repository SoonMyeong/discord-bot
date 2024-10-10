package com.soonworld.bot.job;

import com.soonworld.bot.config.AppConfig;
import com.soonworld.bot.user.Users;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.TimerTask;

public class WorkCheckJob extends TimerTask {
    private final TextChannel channel;

    public WorkCheckJob(TextChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        AppConfig.userMap.forEach((k,v) -> {
            if(!Users.set.contains(k)) {
                channel.sendMessage(v + "님 과제를 안하셨네? 벌금 대상자 입니다. 1,000원 입니다.").queue();
            }
        });
        Users.set.clear();
    }
}
