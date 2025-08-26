package com.adlifehub.adlife.mapper;

import com.adlifehub.adlife.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    void insert(Report r);

    List<Report> list(@Param("status") String status, @Param("type") String type, @Param("offset") int offset, @Param("limit") int limit);

    int count(@Param("status") String status, @Param("type") String type);

    Report findById(@Param("id") Long id);

    void updateStatus(@Param("id") Long id, @Param("status") String status);
}
