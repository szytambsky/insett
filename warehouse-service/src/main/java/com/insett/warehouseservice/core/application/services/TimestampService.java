package com.insett.warehouseservice.core.application.services;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class TimestampService {

    private final EntityManager entityManager;

    /**
     * Save entity using DB-generated timestamps.
     * Ensures created_at and updated_at are populated after save.
     */
    @Transactional
    public <T, ID> T persist(JpaRepository<T, ID> repository, T entity) {
        T saved = repository.saveAndFlush(entity);
        entityManager.refresh(saved);
        return saved;
    }

    /**
     * Update entity and refresh timestamps.
     */
    @Transactional
    public <T, ID> T updateWithTimestamps(JpaRepository<T, ID> repository, T entity) {
        T saved = repository.saveAndFlush(entity);
        entityManager.refresh(saved);
        return saved;
    }

    /**
     * Save a list of entities using DB timestamps.
     * Refreshes each entity after flush.
     */
    public <T, ID> List<T> saveAllWithTimestamps(
            JpaRepository<T, ID> repository,
            List<T> entities) {
        List<T> savedMany = repository.saveAll(entities);
        repository.flush();
        for (T entity : savedMany) {
            entityManager.refresh(entity);
        }
        return savedMany;
    }

}
