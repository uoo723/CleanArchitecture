package com.umanji.umanjiapp.model.mapper

import com.umanji.umanjiapp.domain.entity.Portal
import com.umanji.umanjiapp.model.PortalModel

/**
 * Mapper for [Portal] and [PortalModel]
 */
object PortalModelMapper : ModelMapper<Portal, PortalModel> {

    override fun transform(domainEntity: Portal): PortalModel {
        return PortalModel(
                id = domainEntity.id,
                portalName = domainEntity.portalName,
                country = domainEntity.adminDistrict?.country ?: "",
                locality = domainEntity.adminDistrict?.locality ?: "",
                subLocality1 = domainEntity.adminDistrict?.subLocality1 ?: "",
                subLocality2 = domainEntity.adminDistrict?.subLocality2 ?: "",
                latitude = domainEntity.location?.latitude ?: 0.0,
                longitude = domainEntity.location?.longitude ?: 0.0
        )
    }
}
