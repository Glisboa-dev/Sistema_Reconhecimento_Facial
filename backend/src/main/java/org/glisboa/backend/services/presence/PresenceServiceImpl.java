package org.glisboa.backend.services.presence;

import lombok.AllArgsConstructor;
import org.glisboa.backend.domain.repositories.presence.PresenceRepository;
import org.glisboa.backend.domain.repositories.record.RecordRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PresenceServiceImpl implements PresenceService{
    private final PresenceRepository presenceRepository;
    private final RecordRepository recordRepository;

    @Override
    public void registerPresence(Integer recordId) {
        var record = recordRepository.findById(recordId).orElseThrow();

    }
}
