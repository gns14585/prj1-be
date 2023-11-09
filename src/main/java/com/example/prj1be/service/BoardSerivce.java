package com.example.prj1be.service;

import com.example.prj1be.domain.Board;
import com.example.prj1be.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class BoardSerivce {

    private final BoardMapper mapper;

    public boolean save(Board board) {
        return  mapper.insert(board) == 1;
    }
}