package org.chzzk.howmeet.domain.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.common.util.DisplayFormat.DISPLAY_NAME_FORMAT;

class NicknameTest {
    @ParameterizedTest(name = DISPLAY_NAME_FORMAT)
    @DisplayName("닉네임은 2~10자만 가능")
    @CsvSource(value = {
            "2글자 미만, ㅁ",
            "10글자 이상, aaaaaaaaaaaaaaaaa"
    })
    public void invalidLength(final String displayName, final String value) throws Exception {
        assertThatThrownBy(() -> Nickname.from(value))
                .isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest(name = DISPLAY_NAME_FORMAT)
    @DisplayName("알파벳(A~Z, a~z), 숫자(0~9), 언더스코어(_), 하이픈(-), 한글(가-힣) 외에 문자가 포함되면 예외 발생")
    @CsvSource(value = {
            "특수 문자, !hello",
            "공백 문자, hel lo",
            "제어 문자, hel\tlo",
            "기타 언어, '中文1",
            "이모지, \uD83D\uDE0A",
            "수학 기호, √hello",
            "기타, ©hello"
    })
    public void invalidCharacters(final String displayName, final String value) throws Exception {
        assertThatThrownBy(() -> Nickname.from(value))
                .isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest(name = DISPLAY_NAME_FORMAT)
    @DisplayName("언더스코어(_), 하이픈(-)은 다른 문자나 숫자와 함께 사용")
    @CsvSource(value = {
            "언더스코어(_)만 사용, ______",
            "하이픈(-)만 사용, ------"
    })
    public void invalidHyphenOrUnderscore(final String displayName, final String value) throws Exception {
        assertThatThrownBy(() -> Nickname.from(value))
                .isInstanceOf(RuntimeException.class);
    }
}