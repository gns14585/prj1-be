package com.example.prj1be.mapper;

import com.example.prj1be.domin.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
            INSERT INTO comment (boardId, comment, memberId)
            VALUES (#{boardId}, #{comment}, #{memberId})
            """)
    int insert(Comment comment);


}
