package org.chzzk.howmeet.domain.temporary.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.chzzk.howmeet.common.util.DisplayFormat.DISPLAY_NAME_FORMAT;

class PasswordValidatorTest {
    PasswordValidator passwordValidator = new PasswordValidator();

    @ParameterizedTest(name = DISPLAY_NAME_FORMAT)
    @DisplayName("비밀번호는 숫자, 영문 조합으로 구성")
    @CsvSource(value = {
            "숫자만, 1234",
            "문자만, hello",
            "숫자 + 문자, hello123"
    })
    public void valid(final String displayName, final String value) throws Exception {
        assertThatCode(() -> passwordValidator.validate(value))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest(name = DISPLAY_NAME_FORMAT)
    @DisplayName("비밀번호는 숫자, 영문 4자리 이상")
    @CsvSource(
            "4자리 미만, asd"
    )
    public void invalidLength(final String displayName, final String value) throws Exception {
        assertThatThrownBy(() -> passwordValidator.validate(value))
                .isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest(name = DISPLAY_NAME_FORMAT)
    @DisplayName("비밀번호는 숫자, 영문외에 문자가 포함되면 예외 발생")
    @CsvSource(value = {
            "언더스코어(_), _hello",
            "하이픈(-), -hello",
            "특수 문자, !hello",
            "공백 문자, hel lo",
            "제어 문자, hel\tlo",
            "기타 언어, '中文1",
            "이모지, \uD83D\uDE0Ahello",
            "수학 기호, √hello",
            "기타, ©hello"
    })
    public void invalidCharacters(final String displayName, final String value) throws Exception {
        assertThatThrownBy(() -> passwordValidator.validate(value))
                .isInstanceOf(RuntimeException.class);
    }
}