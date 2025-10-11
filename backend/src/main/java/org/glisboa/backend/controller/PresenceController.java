package org.glisboa.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.employee.post.Post;
import org.glisboa.backend.domain.models.student.grade.Grade;
import org.glisboa.backend.dto.request.presence.filter.SearchPresenceFilter;
import org.glisboa.backend.exception.auth.AuthException;
import org.glisboa.backend.service.presence.PresenceService;
import org.glisboa.backend.utils.api.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presences")
public class PresenceController {

    private final PresenceService presenceService;

    @PostMapping("/register/{id}")
    public ResponseEntity<ApiResponse> registerPresence(@PathVariable("id") Integer registerId, HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");

        if ((origin != null && !origin.equals("http://127;0.0.1:8081")) &&
                (referer != null && !referer.startsWith("http://127.0.0.1:8081"))) {
            throw new AuthException("Origem não autorizada");
        }
        presenceService.registerPresence(registerId);
        return ApiResponse.success("Presença registrada com sucesso", null, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchPresences(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @RequestParam(required = false)
            Grade grade,

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            Post post,

            @RequestParam(defaultValue = "true")
            boolean searchStudents,

            Pageable pageable
    ) {

        var presences = presenceService.searchPresences(
                new SearchPresenceFilter(from, to, grade, name, post, searchStudents),
                pageable
        );

        return ApiResponse.success("Presenças encontradas com sucesso", presences, HttpStatus.OK);
    }
}
