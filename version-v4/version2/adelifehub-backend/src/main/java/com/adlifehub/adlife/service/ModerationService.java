package com.adlifehub.adlife.service;

import com.adlifehub.adlife.mapper.ModerationMapper;
import com.adlifehub.adlife.model.ModerationNote;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ModerationService {
    private final ModerationMapper moderationMapper;

    public ModerationService(ModerationMapper moderationMapper) {
        this.moderationMapper = moderationMapper;
    }

    public void addNote(String type, Long id, Long actorId, String note) {
        ModerationNote n = new ModerationNote();
        n.setTargetType(type);
        n.setTargetId(id);
        n.setActorId(actorId);
        n.setNote(note);
        n.setCreatedAt(OffsetDateTime.now());
        moderationMapper.insert(n);
    }

    public List<ModerationNote> list(String type, Long id) {
        return moderationMapper.list(type, id);
    }
}
