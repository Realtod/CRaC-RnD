package org.springframework.samples.petclinic.crac;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.stereotype.Component;

/**
 * Optional resource that triggers periodic checkpoints to reduce restart time
 * after failures or migrations.
 */
@Component
public class PeriodicCheckpointResource implements Resource {

    private ScheduledExecutorService executor;

    public PeriodicCheckpointResource() {
        boolean enabled = Boolean.parseBoolean(System.getenv("ENABLE_PERIODIC_CHECKPOINT"));
        if (enabled) {
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(() -> {
                try {
                    Core.checkpointRestore();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, 30, 300, TimeUnit.SECONDS);
        }
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) {
        boolean enabled = Boolean.parseBoolean(System.getenv("ENABLE_PERIODIC_CHECKPOINT"));
        if (enabled) {
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(() -> {
                try {
                    Core.checkpointRestore();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, 30, 300, TimeUnit.SECONDS);
        }
    }
}
