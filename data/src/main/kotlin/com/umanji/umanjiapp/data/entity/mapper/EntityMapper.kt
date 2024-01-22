package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.domain.entity.Entity

/**
 * The interface that transforms data layer entity to domain layer entity and vice versa.
 *
 * @param DataEntity The data layer entity
 * @param DomainEntity The domain layer entity
 */
internal interface EntityMapper<DataEntity : Entity, DomainEntity : Entity> {

    /**
     * Transform data layer entity to domain layer entity.
     */
    fun transformDomain(dataEntity: DataEntity): DomainEntity

    /**
     * Transform domain layer entity to data layer entity.
     */
    fun transformData(domainEntity: DomainEntity): DataEntity
}
