package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.LocationEntity
import com.umanji.umanjiapp.domain.entity.Location


/**
 * Mapper for [Location] and [LocationEntity].
 */
internal object LocationEntityMapper : EntityMapper<LocationEntity, Location> {

    override fun transformDomain(dataEntity: LocationEntity): Location {
        return Location(dataEntity.coordinates[1], dataEntity.coordinates[0])
    }

    override fun transformData(domainEntity: Location): LocationEntity {
        return LocationEntity(coordinates = listOf(domainEntity.longitude, domainEntity.latitude))
    }
}
