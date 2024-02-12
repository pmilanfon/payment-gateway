package com.payce.paymentgateway.processor.statemachine.lock;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Slf4j
@AllArgsConstructor
public class LockService {
    private final JdbcLockRegistry lockRegistry;

    public void performWithLock(@NotBlank String ref, Runnable operation) {
        Objects.requireNonNull(ref, "Request reference must be specified when locking");
        Lock lock = this.lockRegistry.obtain(ref);
        if (lock.tryLock()) {
            try {
                log.info("Acquired lock for depositRef={}", ref);
                Instant beforeOperation = Instant.now();
                operation.run();
                log.info("Locked operation took: {} ms", Duration.between(beforeOperation, Instant.now()).toMillis());
            } finally {
                log.info("Releasing the lock: {}", ref);
                lock.unlock();
            }
        } else {
            log.warn("{} is already locked", ref);
        }
    }
}
