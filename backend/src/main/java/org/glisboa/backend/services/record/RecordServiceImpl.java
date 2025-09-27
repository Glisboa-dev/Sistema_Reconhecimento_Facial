package org.glisboa.backend.services.record;

import lombok.AllArgsConstructor;
import org.glisboa.backend.domain.repositories.record.RecordRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService{
    private final RecordRepository recordRepository;


}
