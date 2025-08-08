package org.springframework.samples.petclinic.crac;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Logs the current configuration value so that it can be compared after a
 * restore in a different environment.
 */
@Component
public class ConfigDemoRunner implements CommandLineRunner {

    private final ConfigValueHolder holder;
    private final FileResource fileResource;

    public ConfigDemoRunner(ConfigValueHolder holder, FileResource fileResource) {
        this.holder = holder;
        this.fileResource = fileResource;
    }

    @Override
    public void run(String... args) throws Exception {
        fileResource.write("Config on start: " + holder.getValue());
    }
}
