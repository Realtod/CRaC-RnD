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
import java.util.Map;

import org.crac.Context;
import org.crac.Resource;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CRaC {@link Resource} that logs Spring cache sizes during checkpoint and
 * restore.
 *
 * <p>The cache contents are included in the checkpoint image. When the
 * application is restored, entries remain so that warm caches can be observed
 * immediately after restore. In a real deployment caches might need manual
 * invalidation to avoid stale data.</p>
 */
@Component
public class CacheResource implements Resource {

    private static final Logger logger = LoggerFactory.getLogger(CacheResource.class);

    private final CacheManager cacheManager;

    public CacheResource(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) {
        logCaches("before checkpoint");
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) {
        logCaches("after restore");
    }

    private void logCaches(String phase) {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String name : cacheNames) {
            Cache cache = cacheManager.getCache(name);
            Object nativeCache = cache != null ? cache.getNativeCache() : null;
            int size = -1;
            if (nativeCache instanceof Map<?, ?>) {
                size = ((Map<?, ?>) nativeCache).size();
            }
            logger.info("{} - cache '{}' size {}", phase, name,
                    (size >= 0 ? size : "unknown"));
        }
    }
}
