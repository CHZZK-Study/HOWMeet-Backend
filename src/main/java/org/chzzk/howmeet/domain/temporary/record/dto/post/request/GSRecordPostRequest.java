package org.chzzk.howmeet.domain.temporary.record.dto.post.request;

import java.time.LocalDateTime;
import java.util.List;

public record GSRecordPostRequest(Long gsId, List<LocalDateTime> selectTime) {

}
