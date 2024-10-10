package com.soonworld.bot.listener;

import com.soonworld.bot.config.AppConfig;
import com.soonworld.bot.user.Users;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class SlashEventListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if("제출확인".equals(event.getName())) {
            checkHomework(event);
        }
        if("벌금입력".equals(event.getName())) {
            addPrice(event);
        }
        if("벌금확인".equals(event.getName())) {
            event.reply("현재까지 모인 벌금 총 금액은 " + AppConfig.price + "원 입니다. 아자!").queue();
        }
        if("숙제완료".equals(event.getName())) {
            completedHomework(event);
        }
    }

    private void completedHomework(@NotNull SlashCommandInteractionEvent event) {
        final var user = event.getOption("user").getAsString();
        var count = 0;
        for (String s : AppConfig.userMap.values()) {
            if (s.equals(user)) {
                Users.set.add(user);
                count++;
            }
        }
        if(count == 0) {
            event.reply("존재하지 않는 스터디원 입니다.").queue();
        }
        event.reply(user + "가 숙제완료 처리 되었습니다.").queue();
    }

    private void addPrice(SlashCommandInteractionEvent event) {
        long inputPrice = Objects.requireNonNull(event.getOption("price")).getAsLong();
        AppConfig.price += inputPrice;
        event.reply(inputPrice + "원 추가 완료.").queue();
    }

    private void checkHomework(@NotNull SlashCommandInteractionEvent event) {
        if(Users.set.isEmpty()) {
            event.reply("오늘 기준 아무도 과제를 제출하지 않았습니다.").queue();
        }else {
            event.reply("현재까지 과제를 제출한 인원은 아래와 같습니다.").queue();
            event.getHook().sendMessage(Users.set.toString()).queue();
        }
        event.getHook().sendMessage("과제 완료까지 남은 시간" + remainTime()).queue();
    }

    private String remainTime() {
        final var now = LocalDateTime.now();
        var todayAt8AM = now.with(LocalTime.of(8, 0));
        // 현재 시간이 아침 8시 이후라면, 내일 아침 8시를 목표로 설정
        if (now.isAfter(todayAt8AM)) {
            todayAt8AM = todayAt8AM.plusDays(1);
        }
        // 남은 시간 계산
        final var  duration = Duration.between(now, todayAt8AM);
        final var hours = duration.toHours();
        final var minutes = duration.toMinutesPart();
        final var seconds = duration.toSecondsPart();

        return hours+"시간"+minutes+"분"+seconds+"초";
    }
}
