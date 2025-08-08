package org.springframework.samples.petclinic.crac;

import org.crac.Context;
import org.crac.Resource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * CRaC resource that refreshes configuration properties after restore. This
 * allows checkpoint images created in one environment to be restored in another
 * with different settings.
 */
@Component
public class ConfigResource implements Resource {

    private final ConfigurableEnvironment environment;
    private final ConfigValueHolder holder;
    private final FileResource fileResource;

    public ConfigResource(ConfigurableEnvironment environment, ConfigValueHolder holder,
            FileResource fileResource) {
        this.environment = environment;
        this.holder = holder;
        this.fileResource = fileResource;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) {
        // nothing to do before checkpoint
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        holder.reload(environment.getProperty("custom.value", "default"));
        fileResource.write("Config after restore: " + holder.getValue());
    }
}
