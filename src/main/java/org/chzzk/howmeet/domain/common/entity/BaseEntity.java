package org.chzzk.howmeet.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
@ToString
public abstract class BaseEntity {
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "update_at", nullable = true, updatable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // todo 7/20 김민우 : 비회원에 포함되는 엔티티만 넣어도 되지 않을까?
    @Column(name = "disable", nullable = false)
    @ColumnDefault("0")
    private Boolean disable;

    protected BaseEntity() {
        this.disable = false;
    }
}
