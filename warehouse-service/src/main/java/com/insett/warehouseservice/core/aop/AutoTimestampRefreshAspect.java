package com.insett.warehouseservice.core.aop;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AutoTimestampRefreshAspect {

    private final EntityManager entityManager;

    @AfterReturning(
            pointcut = "execution(* com.insett..*Repository.save(..))",
            returning = "entity"
    )
    public void flushAndRefreshAfterUpdateOrSave(Object entity) {
        System.out.println("Flush after update or refresh");
        if (entity == null) return;
        var needsRefresh = Arrays.stream(entity.getClass()
                        .getDeclaredFields())
                .anyMatch(field ->
                        field.isAnnotationPresent(UpdateTimestamp.class)
                                && field.getAnnotation(UpdateTimestamp.class).source() == SourceType.DB
                );
        if (needsRefresh) {
            entityManager.flush();
            entityManager.refresh(entity);
        }
    }
}
