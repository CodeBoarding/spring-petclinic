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
package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.samples.petclinic.system.CacheOperations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OwnerDirectorySynchronizerTests {

	@Test
	void ownerAggregateChangesInvalidateCachedSearches() {
		CacheOperations cacheOperations = mock(CacheOperations.class);
		Cache ownerSearchCache = mock(Cache.class);
		given(cacheOperations.ownerSearchCache()).willReturn(ownerSearchCache);
		OwnerDirectorySynchronizer synchronizer = new OwnerDirectorySynchronizer(cacheOperations);

		synchronizer.ownerAggregateChanged();

		verify(cacheOperations).evict(ownerSearchCache);
	}

}
