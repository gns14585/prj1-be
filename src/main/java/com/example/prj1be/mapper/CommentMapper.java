package com.example.prj1be.mapper;

import com.example.prj1be.domain.Member;
import com.example.prj1be.domin.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
            INSERT INTO comment (boardId, comment, memberId)
            VALUES (#{boardId}, #{comment}, #{memberId})
            """)
    int insert(Comment comment);

    @Select("""
            SELECT 
                c.id, 
                c.comment, 
                c.inserted, 
                c.boardId, 
                c.memberId, 
                m.nickName memberNickName
            FROM comment C JOIN member m ON C.memberId = m.id
            WHERE boardId = #{boardId}
            ORDER BY C.id DESC ;
            """)
    List<Comment> selectByBoardId(Integer boardId);

    @Delete("""
            DELETE FROM comment
            WHERE id = #{id}
            """)
    int deleteById(Integer id);

    @Select("""
            SELECT * FROM comment
            WHERE id = #{id}
            """)
    Comment selectById(Integer id);

    @Update("""
            UPDATE comment
            SET comment = #{comment}
            WHERE id = #{id}
            """)
    int update(Comment comment);
}
