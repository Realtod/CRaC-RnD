package org.springframework.samples.petclinic.crac;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.stereotype.Service;

/**
 * Simple service demonstrating caching of Owners by id.
 */
@Service
public class OwnerCachingService {

    private final OwnerRepository owners;

    public OwnerCachingService(OwnerRepository owners) {
        this.owners = owners;
    }

    @Cacheable("owners")
    public Owner findOwner(int id) {
        return owners.findById(id).orElse(null);
    }
}
