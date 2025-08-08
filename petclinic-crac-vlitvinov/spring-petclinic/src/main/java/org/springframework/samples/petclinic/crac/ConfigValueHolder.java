package org.springframework.samples.petclinic.crac;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Holds a configurable value so that changes across environments can be
 * observed when restoring from a checkpoint.
 */
@Component
public class ConfigValueHolder {

    @Value("${custom.value:default}")
    private String value;

    public String getValue() {
        return value;
    }

    public void reload(String newValue) {
        this.value = newValue;
    }
}
