package com.umanji.umanjiapp.data.repository.datasource.geo

import com.umanji.umanjiapp.data.entity.PortalEntity
import com.umanji.umanjiapp.domain.entity.Location
import io.reactivex.Single


/**
 * Interface that represents a data store from where geographic data is retrieved.
 */
internal interface GeoDataStore {

    /**
     * Get a [Single] which will emit a [PortalEntity].
     *
     * @param location [Location] that contains latitude and longitude.
     */
    fun portal(location: Location): Single<PortalEntity>

    /**
     * Get a [Single] which will emit a [PortalEntity].
     *
     * @param id portal id
     */
    fun portal(id: String): Single<PortalEntity>
}
