package org.chzzk.howmeet.domain.regular.record.dto.post;

import java.time.LocalDateTime;
import java.util.List;

public record MSRecordPostRequest(Long msId, List<LocalDateTime> selectTime) {

}
