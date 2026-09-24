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

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lightweight operational endpoints for checking and refreshing owner-search data.
 */
@RestController
@RequestMapping("/manage/cache/owner-search")
class CacheDiagnosticsController {

	private final CacheOperations cacheOperations;

	CacheDiagnosticsController(CacheOperations cacheOperations) {
		this.cacheOperations = cacheOperations;
	}

	@GetMapping
	CacheOperations.CacheStatus status() {
		return this.cacheOperations.ownerSearchStatus();
	}

	@DeleteMapping
	Map<String, Object> evict() {
		boolean evicted = this.cacheOperations.evictOwnerSearch();
		return Map.of("cache", CacheOperations.OWNER_SEARCH_CACHE, "evicted", evicted);
	}

}
