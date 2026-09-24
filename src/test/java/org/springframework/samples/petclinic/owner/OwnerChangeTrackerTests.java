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

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.petclinic.system.CacheConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OwnerChangeTrackerTests {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private CacheConfiguration cacheConfiguration;

	@Mock
	private CacheManager cacheManager;

	@InjectMocks
	private OwnerChangeTracker changeTracker;

	@Test
	void ownerUpdatesAreWrittenAndSearchResultsAreRefreshed() {
		Owner owner = new Owner();
		owner.setId(7);

		this.changeTracker.ownerUpdated(owner);

		verify(this.jdbcTemplate).update(
				eq("insert into owner_changes (owner_id, change_type, summary, changed_at) values (?, ?, ?, ?)"), eq(7),
				eq("OWNER_UPDATED"), eq("Contact details updated"), any(Timestamp.class));
		verify(this.cacheConfiguration).refreshOwnerData(this.cacheManager);
	}

}
