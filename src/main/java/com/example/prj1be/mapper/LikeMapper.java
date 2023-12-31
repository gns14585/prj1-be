package com.example.prj1be.mapper;

import com.example.prj1be.domain.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    @Delete("""
        DELETE FROM boardLike
        WHERE boardId = #{boardId}
          AND memberId = #{memberId}
        """)
    int delete(Like like);

    @Insert("""
        INSERT INTO boardLike (boardId, memberId)
        VALUES (#{boardId}, #{memberId})
        """)
    int insert(Like like);

    @Select("""
        SELECT COUNT(id) FROM boardLike
        WHERE boardId = #{boardId}
        """)
    int countByBoardId(Integer boardId);

    @Select("""
        SELECT * 
        FROM boardLike
        WHERE 
                boardId = #{boardId}
            AND memberId = #{memberId}   
        """)
    Like selectByBoardIdAndMemberId(Integer boardId, String memberId);

    // 좋아요 레코드 지우기
    @Delete("""
            DELETE FROM boardlike
            WHERE boardId = #{boardId}
            """)
    int deleteByBoardId(Integer boardId);

    // 좋아요 한 멤버 삭제
    @Delete("""
            DELETE FROM boardlike
            WHERE memberId = #{memberId}
            """)
    int deleteByMemberId(String memberId);
}
