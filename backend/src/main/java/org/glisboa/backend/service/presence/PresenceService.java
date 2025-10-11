package org.glisboa.backend.service.presence;

import org.glisboa.backend.dto.request.presence.filter.SearchPresenceFilter;
import org.glisboa.backend.dto.response.presence.PresenceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

public interface PresenceService {
    void registerPresence(Integer registerId);
    PagedModel<PresenceResponse> searchPresences(SearchPresenceFilter searchPresenceFilter, Pageable pageable);
}
