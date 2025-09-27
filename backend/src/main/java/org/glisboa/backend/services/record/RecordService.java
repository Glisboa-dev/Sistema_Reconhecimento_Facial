package org.glisboa.backend.services.record;

import org.glisboa.backend.dto.record.CreateRecordDTO;

public interface RecordService {
     <T> void createRecord(CreateRecordDTO createRecordDTO, T createEntityDTO);
}
