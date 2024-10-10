package com.soonworld.bot;

import com.soonworld.bot.config.AppConfig;
import com.soonworld.bot.job.WorkCheckJob;
import com.soonworld.bot.listener.MonitoringListener;
import com.soonworld.bot.listener.SlashEventListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Timer;

public class WorkCheckBotApplication {
    public static void main(String[] args) throws InterruptedException {
        final var config = new AppConfig();
        final var timer = new Timer();
        final var builder = JDABuilder.createDefault(config.getToken())
                .setActivity(Activity.playing("감시"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MonitoringListener())
                .addEventListeners(new SlashEventListener());
        final var jda = builder.build();
        jda.awaitReady();

        final var channel = jda.getTextChannelById(MonitoringListener.MONITORING_CHANNEL_ID);
        final var morning = LocalDateTime.now().with(LocalTime.of(8, 0));
        timer.scheduleAtFixedRate(new WorkCheckJob(channel),
                java.util.Date.from(morning.atZone(ZoneId.systemDefault()).toInstant()),
                Duration.ofDays(1).toMillis()
        );

        jda.updateCommands().addCommands(
                Commands.slash("제출확인","숙제 제출 현황을 확인 합니다."),
                Commands.slash("숙제완료","수동으로 입력한 유저를 숙제 완료 처리 합니다.")
                        .addOption(OptionType.STRING, "user", "유저명", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("벌금입력","벌금을 입력 합니다.")
                        .addOption(OptionType.INTEGER, "price", "금액", true),
                Commands.slash("벌금확인","벌금 총액을 출력 합니다.")
        ).queue(
                success -> System.out.println("슬래시 명령어 등록 성공!"),
                failure -> System.err.println("슬래시 명령어 등록 실패: " + failure.getMessage())
        );
    }
}
