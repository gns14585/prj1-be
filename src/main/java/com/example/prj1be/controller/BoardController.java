package com.example.prj1be.controller;

import com.example.prj1be.domain.Board;
import com.example.prj1be.service.BoardSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardSerivce service;

    @PostMapping("add")
    public void add(@RequestBody Board board) {

        service.save(board);
    }


}
