package org.chzzk.howmeet.domain.regular.record.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.domain.common.auth.annotation.Authenticated;
import org.chzzk.howmeet.domain.common.auth.model.AuthPrincipal;
import org.chzzk.howmeet.domain.regular.record.dto.get.MSRecordGetRequest;
import org.chzzk.howmeet.domain.regular.record.dto.post.MSRecordPostRequest;
import org.chzzk.howmeet.domain.regular.record.service.MSRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/ms-record")
@RestController
public class MSRecordController {

    final MSRecordService msRecordService;

    @PostMapping
    public ResponseEntity<?> postMSRecord(@RequestBody final MSRecordPostRequest msRecordPostRequest,
            @Authenticated final AuthPrincipal authPrincipal) {
        msRecordService.postMSRecord(msRecordPostRequest, authPrincipal);
        return ResponseEntity.created(URI.create("/ms-record/" + msRecordPostRequest.msId()))
                .body("일정내역이 성공적으로 추가되었습니다.");
    }

    @GetMapping
    public ResponseEntity<?> getMSRecord(@RequestBody final MSRecordGetRequest msRecordGetRequest,
            @Authenticated final AuthPrincipal authPrincipal) {
        return ResponseEntity.ok(msRecordService.getMSRecord(msRecordGetRequest, authPrincipal));
    }
}
