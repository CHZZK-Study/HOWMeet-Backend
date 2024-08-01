package org.chzzk.howmeet.global.config;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(showSql = false, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@Import({TestJpaAuditingConfig.class, TestQueryDslConfig.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestPropertySource(locations = "classpath:application-test.yml")
public @interface RepositoryTest {
}
