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
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.petclinic.system.CacheConfiguration;
import org.springframework.stereotype.Service;

/**
 * Records the activity feed shown to front-desk staff and refreshes cached owner search
 * projections after mutations.
 */
@Service
public class OwnerChangeTracker {

	private final JdbcTemplate jdbcTemplate;

	private final CacheConfiguration cacheConfiguration;

	private final CacheManager cacheManager;

	public OwnerChangeTracker(JdbcTemplate jdbcTemplate, CacheConfiguration cacheConfiguration,
			CacheManager cacheManager) {
		this.jdbcTemplate = jdbcTemplate;
		this.cacheConfiguration = cacheConfiguration;
		this.cacheManager = cacheManager;
	}

	public void ownerCreated(Owner owner) {
		recordChange(owner.getId(), "OWNER_CREATED", "Owner record created");
	}

	public void ownerUpdated(Owner owner) {
		recordChange(owner.getId(), "OWNER_UPDATED", "Contact details updated");
	}

	public void petAdded(Owner owner, Pet pet) {
		recordChange(owner.getId(), "PET_ADDED", "Pet added: " + pet.getName());
	}

	public void petUpdated(Owner owner, Pet pet) {
		recordChange(owner.getId(), "PET_UPDATED", "Pet details updated: " + pet.getName());
	}

	public void visitBooked(Owner owner, Visit visit) {
		recordChange(owner.getId(), "VISIT_BOOKED", "Visit booked: " + visit.getDescription());
	}

	public List<OwnerChange> changesFor(int ownerId) {
		return this.jdbcTemplate.query("""
				select change_type, summary, changed_at
				from owner_changes
				where owner_id = ?
				order by changed_at desc, id desc
				""", (rs, rowNum) -> new OwnerChange(rs.getString("change_type"), rs.getString("summary"),
				rs.getTimestamp("changed_at").toLocalDateTime()), ownerId);
	}

	private void recordChange(Integer ownerId, String changeType, String summary) {
		LocalDateTime changedAt = LocalDateTime.now();
		this.jdbcTemplate.update(
				"insert into owner_changes (owner_id, change_type, summary, changed_at) values (?, ?, ?, ?)", ownerId,
				changeType, summary, Timestamp.valueOf(changedAt));
		this.cacheConfiguration.refreshOwnerData(this.cacheManager);
	}

}
