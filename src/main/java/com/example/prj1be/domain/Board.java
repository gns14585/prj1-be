package com.example.prj1be.domain;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Data
public class Board {
    private Integer id;
    private String title;
    private String content;
    private String writer;
    private String nickName;
    private LocalDateTime inserted;
    private Integer countComment;
    private Integer countLike;

    // 날짜 형식 / ex) 몇년전, 몇달전, 며칠전, 몇시간전, 그외
    // Period : 몇년, 몇달, 며칠 날짜단위
    // Duration : 몇시간전, 몇분전 시간단위
    public String getAgo() {
        LocalDateTime now = LocalDateTime.now();

        if (inserted.isBefore(now.minusYears(1))) {
            Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
            return between.get(ChronoUnit.YEARS) + "년 전";
        } else if (inserted.isBefore(now.minusMonths(1))) {
            Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
            return between.get(ChronoUnit.MONTHS) + "달 전";
        } else if (inserted.isBefore(now.minusDays(1))) {
            Period between = Period.between(inserted.toLocalDate(), now.toLocalDate());
            return between.get(ChronoUnit.DAYS) + "일 전";
        } else if (inserted.isBefore(now.minusHours(1))) {
            Duration between = Duration.between(inserted, now);
            return (between.getSeconds() / 60 / 60) + "시간 전";
        } else if (inserted.isBefore(now.minusMinutes(1))) {
            Duration between = Duration.between(inserted, now);
            return (between.getSeconds() / 60) + "분 전";
        } else {
            Duration between = Duration.between(inserted, now);
            return between.getSeconds() + "초 전";
        }
    }
}


