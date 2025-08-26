package com.adlifehub.adlife.mapper;

import com.adlifehub.adlife.model.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface PostMapper {
    Post findById(@Param("id") Long id);

    List<Post> listActive(@Param("offset") int offset, @Param("limit") int limit);

    int countActive();

    List<Post> listByAuthor(@Param("authorId") Long authorId, @Param("status") String status, @Param("offset") int offset, @Param("limit") int limit);

    int countByAuthor(@Param("authorId") Long authorId, @Param("status") String status);

    void insert(Post p);

    void update(Post p);

    void softDelete(@Param("id") Long id, @Param("deletedAt") OffsetDateTime deletedAt, @Param("deletedBy") Long deletedBy, @Param("reason") String reason);

    void restore(@Param("id") Long id);
}
