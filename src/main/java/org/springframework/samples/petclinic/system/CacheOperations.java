/*
 * Copyright 2012-2025 the original author or authors.
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
package org.springframework.samples.petclinic.system;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Operational boundary for inspecting and invalidating application caches.
 */
@Service
public class CacheOperations {

	public static final String OWNER_SEARCH_CACHE = "ownerSearch";

	private final CacheManager cacheManager;

	public CacheOperations(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * Invalidate all cached owner-directory searches.
	 * @return {@code true} when the configured cache was available
	 */
	public boolean evictOwnerSearch() {
		return evict(ownerSearchCache());
	}

	/**
	 * Evict every entry from the supplied cache.
	 * @param cache the cache selected by the caller
	 * @return {@code true} when a cache was supplied
	 */
	public boolean evict(Cache cache) {
		if (cache == null) {
			return false;
		}
		cache.clear();
		return true;
	}

	/**
	 * Report whether the owner-search cache is currently available.
	 */
	public CacheStatus ownerSearchStatus() {
		return new CacheStatus(OWNER_SEARCH_CACHE, ownerSearchCache() != null);
	}

	/**
	 * Resolve the cache used by the owner directory.
	 */
	public Cache ownerSearchCache() {
		return this.cacheManager.getCache(OWNER_SEARCH_CACHE);
	}

	public record CacheStatus(String name, boolean available) {
	}

}
