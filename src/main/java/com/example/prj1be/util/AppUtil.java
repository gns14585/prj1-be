package com.example.prj1be.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class AppUtil { // 공통적으로 사용할 UtilClass 생성
    public static String getAgo(LocalDateTime a, LocalDateTime b) {

        // 날짜 형식 / ex) 몇년전, 몇달전, 며칠전, 몇시간전, 그외
        // Period : 몇년, 몇달, 며칠 날짜단위
        // Duration : 몇시간전, 몇분전 시간단위

        if (a.isBefore(b.minusYears(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.YEARS) + "년 전";

        } else if (a.isBefore(b.minusMonths(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.MONTHS) + "달 전";

        } else if (a.isBefore(b.minusDays(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.DAYS) + "일 전";

        } else if (a.isBefore(b.minusHours(1))) {
            Duration between = Duration.between(a, b);
            return (between.getSeconds() / 60 / 60) + "시간 전";

        } else if (a.isBefore(b.minusMinutes(1))) {
            Duration between = Duration.between(a, b);
            return (between.getSeconds() / 60) + "분 전";

        } else {
            Duration between = Duration.between(a, b);
            return between.getSeconds() + "초 전";
        }
    }
}
