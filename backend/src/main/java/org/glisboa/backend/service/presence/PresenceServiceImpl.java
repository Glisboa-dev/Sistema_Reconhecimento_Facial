package org.glisboa.backend.service.presence;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.presence.Presence;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.repositories.presence.PresenceRepository;
import org.glisboa.backend.domain.specifications.presence.PresenceSpecification;
import org.glisboa.backend.dto.request.presence.filter.SearchPresenceFilter;
import org.glisboa.backend.dto.response.presence.PresenceResponse;
import org.glisboa.backend.service.record.RecordService;
import org.glisboa.backend.utils.domain.repo.RepositoryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PresenceServiceImpl implements PresenceService{
    private final PresenceRepository presenceRepo;
    private final RecordService recordService;

    @Transactional
    @Override
    public void registerPresence(Integer registerId) {
        System.out.println("Registering presence for ID: " + registerId);
     var record = recordService.getRecordById(registerId);
     var newPresence = new Presence(record);
        System.out.println("New presence created at: " + newPresence.getCreatedAt());
        System.out.println(record.getId());

     registerPresenceTimeOut(record, newPresence);
        System.out.println("Presences after timeout check: " + record.getPresences().size());

        RepositoryUtils.saveEntity(presenceRepo,newPresence);
        System.out.println("Presence registered successfully.");
    }

    @Override
    public PagedModel<PresenceResponse> searchPresences(SearchPresenceFilter searchPresenceFilter, Pageable pageable) {
        Page<Presence> presences = presenceRepo.findAll(
                PresenceSpecification.fromSearchPresences(searchPresenceFilter),
                pageable
        );

        List<PresenceResponse> dtoList = presences.stream()
                .map(p -> new PresenceResponse(
                        p.getId(),
                        p.getCreatedAt(),
                        p.getRecord().getName()
                ))
                .toList();

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                pageable.getPageSize(),
                presences.getNumber(),
                presences.getTotalElements(),
                presences.getTotalPages()
        );

        return PagedModel.of(dtoList, metadata);
    }

    private void registerPresenceTimeOut(Record record, Presence newPresence){
        var presences = record.getPresences();
        if(presences.isEmpty()) return;

        if (Duration.between(presences.getLast().getCreatedAt(), newPresence.getCreatedAt()).toMinutes() > 5) {
            presences.add(newPresence);
        }

    }
}
