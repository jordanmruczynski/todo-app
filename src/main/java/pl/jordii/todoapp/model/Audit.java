package pl.jordii.todoapp.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@Embeddable
abstract class Audit {

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preMerge() {
        updatedOn = LocalDateTime.now();
    }
}
