package com.example.prj1be.mapper;

import com.example.prj1be.domain.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {
    @Insert("""
        INSERT INTO board (title, content, writer)
        VALUES (#{title}, #{content}, #{writer})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Board board);

    @Select("""
        SELECT b.id,
               b.title,
               b.writer,
               m.nickName,
               b.inserted,
               COUNT(DISTINCT c.id) countComment,
               COUNT(DISTINCT l.id) ,
               COUNT(DISTINCT f.id) countFile
        FROM board b JOIN member m ON b.writer = m.id
                     LEFT JOIN comment c ON b.id = c.boardId
                     LEFT JOIN boardLike l ON b.id = l.boardId
                     LEFT JOIN boardfile f ON b.id = f.boardId
        # keyword를 추가해서 검색했을때 해당 내용이 나오도록
        WHERE b.content LIKE #{keyword}
            OR b.title LIKE #{keyword}
        GROUP BY b.id
        ORDER BY b.id DESC
        LIMIT #{from}, 10
        """) // 페이징 10페이지씩 보이게 LIMIT 사용
    List<Board> selectAll(Integer from, String keyword);

    @Select("""
        SELECT b.id,
               b.title, 
               b.content, 
               b.writer, 
               m.nickName,
               b.inserted
        FROM board b JOIN member m ON b.writer = m.id
        WHERE b.id = #{id}
        """)
    Board selectById(Integer id);

    @Delete("""
        DELETE FROM board
        WHERE id = #{id}
        """)
    int deleteById(Integer id);

    @Update("""
        UPDATE board
        SET title = #{title},
            content = #{content}
        WHERE id = #{id}
        """)
    int update(Board board);


    @Delete("""
        DELETE FROM board
        WHERE writer = #{writer}
        """)

    int deleteByWriter(String writer);

    @Select("""
        SELECT id
        FROM board
        WHERE writer = #{id}
        """)
    List<Integer> selectIdListByMemberId(String writer);

    // 총 게시물 숫자 파악
    @Select("""
            SELECT COUNT(*) FROM board
            WHERE title LIKE #{keyword}
                OR content LIKE #{keyword}
            """)
    int countAll(String s);
}
