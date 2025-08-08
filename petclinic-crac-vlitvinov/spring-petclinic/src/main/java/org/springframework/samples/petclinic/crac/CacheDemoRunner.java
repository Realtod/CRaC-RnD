/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.crac;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Warms up the cache and writes timings to a file so that behaviour can be
 * observed after a CRaC restore.
 */
@Component
public class CacheDemoRunner implements CommandLineRunner {

    private final CacheableService service;

    private final OwnerCachingService ownerService;

    private final FileResource fileResource;

    public CacheDemoRunner(CacheableService service, OwnerCachingService ownerService,
            FileResource fileResource) {
        this.service = service;
        this.ownerService = ownerService;
        this.fileResource = fileResource;
    }

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();
        service.slow(1);
        long firstCall = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        service.slow(1);
        long secondCall = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        ownerService.findOwner(1);
        long ownerFirst = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        ownerService.findOwner(1);
        long ownerSecond = System.currentTimeMillis() - start;

        fileResource.write("Slow call took " + firstCall + " ms then " + secondCall +
                " ms; Owner lookup took " + ownerFirst + " ms then " + ownerSecond + " ms");
    }
}
