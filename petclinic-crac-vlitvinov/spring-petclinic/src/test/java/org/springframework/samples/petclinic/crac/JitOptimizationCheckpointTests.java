package org.springframework.samples.petclinic.crac;

import static org.assertj.core.api.Assertions.assertThat;

import org.crac.Core;
import org.junit.jupiter.api.Test;

/**
 * Verifies that JIT-optimized methods remain optimized after a CRaC restore.
 * The test warms up a computation-heavy method to trigger JIT compilation,
 * captures a checkpoint and, upon restore, checks that the method continues
 * to run with the warmed-up performance.
 */
class JitOptimizationCheckpointTests {

    private long runComputation() {
        long sum = 0;
        for (int i = 0; i < 10_000_000; i++) {
            sum += Math.sqrt(i);
        }
        return sum;
    }

    private long measure() {
        long start = System.nanoTime();
        runComputation();
        return System.nanoTime() - start;
    }

    @Test
    void jitOptimizationsPersistAfterRestore() throws Exception {
        long cold = measure();
        // warm up JIT
        for (int i = 0; i < 5; i++) {
            runComputation();
        }
        long warm = measure();
        assertThat(warm).isLessThan(cold);

        try {
            Core.checkpointRestore();
        }
        catch (UnsupportedOperationException ex) {
            // environment does not support CRaC; skip post-restore verification
            return;
        }

        long restored = measure();
        assertThat(restored).isLessThan(cold);
        assertThat(restored).isLessThan(warm * 2);
    }
}

