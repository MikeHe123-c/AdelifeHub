package com.adlifehub.adlife.service;

import com.adlifehub.adlife.mapper.ReportMapper;
import com.adlifehub.adlife.model.Report;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ReportService {
    private final ReportMapper reportMapper;

    public ReportService(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    public Report create(String type, Long targetId, Long reporterId, String reasonCode, String reasonText) {
        Report r = new Report();
        r.setTargetType(type);
        r.setTargetId(targetId);
        r.setReporterId(reporterId);
        r.setReasonCode(reasonCode);
        r.setReasonText(reasonText);
        r.setStatus("pending");
        r.setCreatedAt(OffsetDateTime.now());
        reportMapper.insert(r);
        return r;
    }

    public List<Report> list(String status, String type, int offset, int limit) {
        return reportMapper.list(status, type, offset, limit);
    }

    public int count(String status, String type) {
        return reportMapper.count(status, type);
    }

    public Report findById(Long id) {
        return reportMapper.findById(id);
    }

    public void updateStatus(Long id, String status) {
        reportMapper.updateStatus(id, status);
    }
}
