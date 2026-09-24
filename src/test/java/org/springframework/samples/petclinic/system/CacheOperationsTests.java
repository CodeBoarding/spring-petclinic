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

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CacheOperationsTests {

	@Test
	void evictsTheOwnerSearchCache() {
		CacheManager cacheManager = mock(CacheManager.class);
		Cache cache = mock(Cache.class);
		given(cacheManager.getCache(CacheOperations.OWNER_SEARCH_CACHE)).willReturn(cache);
		CacheOperations operations = new CacheOperations(cacheManager);

		assertThat(operations.evictOwnerSearch()).isTrue();

		verify(cache).clear();
	}

	@Test
	void reportsWhenOwnerSearchCacheIsUnavailable() {
		CacheManager cacheManager = mock(CacheManager.class);
		CacheOperations operations = new CacheOperations(cacheManager);

		assertThat(operations.evictOwnerSearch()).isFalse();
		assertThat(operations.ownerSearchStatus())
			.isEqualTo(new CacheOperations.CacheStatus(CacheOperations.OWNER_SEARCH_CACHE, false));
	}

}
