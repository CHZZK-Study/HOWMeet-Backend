package org.chzzk.howmeet.domain.regular.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSRequest;
import org.chzzk.howmeet.domain.regular.schedule.dto.MSResponse;
import org.chzzk.howmeet.domain.regular.schedule.dto.ProgressedMSResponse;
import org.chzzk.howmeet.domain.regular.schedule.service.MSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("room/{roomId}")
@RestController
public class MSController {
    private final MSService msService;

    @PostMapping
    public ResponseEntity<ProgressedMSResponse> createMemberSchedule(@PathVariable Long roomId, @RequestBody final MSRequest msRequest) {
        final ProgressedMSResponse progressedMsResponse = msService.createMemberSchedule(roomId, msRequest);
        return ResponseEntity.created(URI.create("/member-schedule/" + progressedMsResponse.id()))
                .build();
    }

    @GetMapping("/{msId}")
    public ResponseEntity<?> getMemberSchedule(@PathVariable Long roomId, @PathVariable Long msId) {
        final MSResponse msResponse = msService.getMemberSchedule(roomId, msId);
        return ResponseEntity.ok(msResponse);
    }

    @DeleteMapping("/{msId}")
    public ResponseEntity<?> deleteGuestSchedule(@PathVariable Long roomId, @PathVariable Long msId) {
        msService.deleteMemberSchedule(roomId, msId);
        return ResponseEntity.ok("Member schedule successfully deleted");
    }
}
