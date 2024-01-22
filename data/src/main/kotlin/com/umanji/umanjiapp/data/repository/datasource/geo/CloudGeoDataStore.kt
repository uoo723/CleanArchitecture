package com.umanji.umanjiapp.data.repository.datasource.geo

import com.umanji.umanjiapp.data.entity.PortalEntity
import com.umanji.umanjiapp.data.network.RestApi
import com.umanji.umanjiapp.domain.entity.Location
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [GeoDataStore] implementation based on connections to the api.
 */
@Singleton
open class CloudGeoDataStore @Inject constructor(
        private val restApi: RestApi
) : GeoDataStore {

    override fun portal(location: Location): Single<PortalEntity> {
        return restApi.portal(location.latitude, location.longitude).map { it.data }
    }

    override fun portal(id: String): Single<PortalEntity> {
        return restApi.portal(id).map { it.data }
    }
}
