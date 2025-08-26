package com.adlifehub.adlife.mapper;

import com.adlifehub.adlife.model.ModerationNote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModerationMapper {
    void insert(ModerationNote note);

    List<ModerationNote> list(@Param("targetType") String targetType, @Param("targetId") Long targetId);
}
