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

import java.util.Collection;

import org.crac.Context;
import org.crac.Resource;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * CRaC {@link Resource} that clears Spring caches after restore.
 *
 * <p>The cache contents are included in the checkpoint image. When the
 * application is restored, entries might be stale. To avoid serving outdated
 * data we invalidate all caches after restore.</p>
 *
 * @author AI
 */
@Component
public class CacheResource implements Resource {

    private final CacheManager cacheManager;

    public CacheResource(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) {
        // Nothing to do; cache state will be stored in the checkpoint.
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String name : cacheNames) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
    }
}
