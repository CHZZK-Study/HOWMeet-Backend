package org.chzzk.howmeet.domain.regular.record.controller;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.annotation.Authenticated;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.auth.annotation.RegularUser;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.service.MSRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/ms-record")
@RestController
public class MSRecordController {

    private final MSRecordService msRecordService;

    @PostMapping
    @RegularUser
    public ResponseEntity<?> postMSRecord(@RequestBody final MSRecordPostRequest msRecordPostRequest,
            @Authenticated final AuthPrincipal authPrincipal) {
        msRecordService.postMSRecord(msRecordPostRequest, authPrincipal);
        return ResponseEntity.created(URI.create("/ms-record/" + msRecordPostRequest.msId()))
                .build();
    }

    @GetMapping("/{roomId}/{msId}")
    @RegularUser
    public ResponseEntity<?> getMSRecord(@PathVariable(value = "roomId") final Long roomId,
                                         @PathVariable(value = "msId") final Long msId ) {
        return ResponseEntity.ok(msRecordService.getMSRecord(roomId, msId));
    }
}
