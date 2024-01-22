package com.umanji.umanjiapp.model.mapper

import com.umanji.umanjiapp.domain.entity.Entity

/**
 * Interface that transforms domain layer entity to model used in presentation layer.
 */
interface ModelMapper<in DomainEntity : Entity, out Model : Entity> {

    /**
     * Transform [DomainEntity] to [Model].
     */
    fun transform(domainEntity: DomainEntity): Model
}
