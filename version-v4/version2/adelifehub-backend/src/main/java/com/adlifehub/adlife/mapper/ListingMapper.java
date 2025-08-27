package com.adlifehub.adlife.mapper;

import com.adlifehub.adlife.model.Listing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface ListingMapper {
    Listing findById(@Param("id") Long id);

    List<Listing> list(@Param("type") String type, @Param("offset") int offset, @Param("limit") int limit);

    int count(@Param("type") String type);

    List<Listing> listByAuthor(@Param("authorId") Long authorId, @Param("status") String status, @Param("offset") int offset, @Param("limit") int limit);

    int countByAuthor(@Param("authorId") Long authorId, @Param("status") String status);

    void insert(Listing l);

    void update(Listing l);

    void softDelete(@Param("id") Long id, @Param("deletedAt") OffsetDateTime deletedAt, @Param("deletedBy") Long deletedBy, @Param("reason") String reason);

    void restore(@Param("id") Long id);
}
