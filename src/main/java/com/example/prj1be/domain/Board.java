package com.example.prj1be.domain;

import com.example.prj1be.util.AppUtil;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    private List<BoardFile> files;


    public String getAgo() { // AppUtll 클래스에 공통 날짜함수를 빼놓을걸 연결시켜줘야함
        return AppUtil.getAgo(inserted, LocalDateTime.now());
    }
}


