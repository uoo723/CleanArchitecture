package com.umanji.umanjiapp.data.repository

import com.umanji.umanjiapp.data.entity.mapper.PortalEntityMapper
import com.umanji.umanjiapp.data.repository.datasource.geo.CloudGeoDataStore
import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Portal
import com.umanji.umanjiapp.domain.repository.GeoRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


/**
 * [GeoRepository] for retrieving geographic data.
 */
@Singleton
class GeoDataRepository @Inject constructor(
        private val cloudGeoDataStore: CloudGeoDataStore
) : GeoRepository {

    override fun portal(location: Location): Single<Portal> {
        return cloudGeoDataStore.portal(location).map(PortalEntityMapper::transformDomain)
    }

    override fun portal(id: String): Single<Portal> {
        return cloudGeoDataStore.portal(id).map(PortalEntityMapper::transformDomain)
    }
}
