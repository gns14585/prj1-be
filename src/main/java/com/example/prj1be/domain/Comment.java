package com.example.prj1be.domain;

import com.example.prj1be.util.AppUtil;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id;
    private Integer boardId;
    private String memberId;
    private String memberNickName;
    private String comment;
    private LocalDateTime inserted;

    public String getAgo() { // AppUtll 클래스에 공통 날짜함수를 빼놓을걸 연결시켜줘야함
        return AppUtil.getAgo(inserted);
    }
}


