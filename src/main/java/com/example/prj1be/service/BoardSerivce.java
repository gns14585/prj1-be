package com.example.prj1be.service;

import com.example.prj1be.domain.Board;
import com.example.prj1be.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardSerivce {

    private final BoardMapper mapper;

    public boolean save(Board board) {
        return  mapper.insert(board) == 1;
    }

    public boolean validate(Board board) {
        if (board == null) {
            return false;
        }
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }
        if (board.getWriter() == null || board.getWriter().isBlank()) {
            return false;
        }

        return true;
    }

    public List<Board> list() {
        return mapper.selectAll();
    }

    public Board get(Integer id) {
        return mapper.selectById(id);
    }

    public boolean remove(Integer id) {
        return mapper.deleteById(id) == 1;
    }

    public boolean update(Board board) {
        return mapper.update(board) == 1;
    }

    public boolean nondate(Board board, Map<String, String> map) {
        if (board == null) {
            return false;
        }
        if (board.getContent() == null || board.getContent().isBlank()) {
            map.put("message", "본문이 비어있습니다.");
            return false;
        }
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            map.put("message", "제목이 비어있습니다.");
            return false;
        }
        if (board.getWriter() == null || board.getWriter().isBlank()) {
            map.put("message", "작성자가 비어있습니다.");
            return false;
        }
        return true;
    }
}
