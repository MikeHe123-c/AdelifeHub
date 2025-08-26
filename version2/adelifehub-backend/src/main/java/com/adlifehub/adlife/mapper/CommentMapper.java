package com.adlifehub.adlife.mapper;

import com.adlifehub.adlife.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> listByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId, @Param("offset") int offset, @Param("limit") int limit);

    int countByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId);

    void insert(Comment c);

    void update(Comment c);

    void softDelete(@Param("id") Long id);
}
