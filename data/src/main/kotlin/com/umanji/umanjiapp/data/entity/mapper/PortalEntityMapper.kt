package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.PortalEntity
import com.umanji.umanjiapp.domain.entity.AdminDistrict
import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.PoliticalType
import com.umanji.umanjiapp.domain.entity.Portal

/**
 * Mapper for [Portal] and [PortalEntity].
 */
internal object PortalEntityMapper : EntityMapper<PortalEntity, Portal> {

    override fun transformDomain(dataEntity: PortalEntity): Portal {
        return Portal(
                id = dataEntity.id,
                politicalType = dataEntity.politicalType.let {
                    when (it) {
                        "locality" -> PoliticalType.LOCALITY
                        "sublocality1" -> PoliticalType.SUB_LOCALITY_1
                        else -> PoliticalType.SUB_LOCALITY_2
                    }
                },
                portalName = dataEntity.portalName,
                adminDistrict = AdminDistrict(
                        country = dataEntity.country,
                        locality = dataEntity.locality,
                        subLocality1 = dataEntity.subLocality1,
                        subLocality2 = dataEntity.subLocality2
                ),
                location = Location(dataEntity.latitude, dataEntity.longitude)
        )
    }

    override fun transformData(domainEntity: Portal): PortalEntity {
        return PortalEntity(
                id = domainEntity.id,
                politicalType = domainEntity.politicalType.let {
                    when (it) {
                        PoliticalType.WORLD -> "world"
                        PoliticalType.COUNTRY -> "country"
                        PoliticalType.LOCALITY -> "locality"
                        PoliticalType.SUB_LOCALITY_1 -> "sublocality1"
                        PoliticalType.SUB_LOCALITY_2 -> "sublocality2"
                    }
                },
                country = domainEntity.adminDistrict?.country ?: "",
                locality = domainEntity.adminDistrict?.locality ?: "",
                subLocality1 = domainEntity.adminDistrict?.subLocality1 ?: "",
                subLocality2 = domainEntity.adminDistrict?.subLocality2 ?: "",
                latitude = domainEntity.location?.latitude ?: 0.0,
                longitude = domainEntity.location?.longitude ?: 0.0
        )
    }
}
