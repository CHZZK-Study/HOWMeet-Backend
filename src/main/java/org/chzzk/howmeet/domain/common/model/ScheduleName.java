package org.chzzk.howmeet.domain.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class ScheduleName {
    private final String value;

    // Todo : JSON 역직렬화에 필요한 생성자를 추가 -> 오류가 잡히지 않아서 chatGpt를 참고하여 추가함, 수정할 수 있는지 확인 필요
    @JsonCreator
    public ScheduleName(@JsonProperty("value") final String value) {
        this.value = value;
    }

    public static ScheduleName from(final String value) {
        return new ScheduleName(value);
    }

    private void validateName(final String value) {

    }
}
