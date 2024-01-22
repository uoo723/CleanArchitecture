package com.umanji.umanjiapp.domain.repository

import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Portal
import io.reactivex.Single


/**
 * Interface that represents a Repository related to geographic data.
 */
interface GeoRepository {

    /**
     * Get a [Single] which will emit a [Portal].
     *
     * @param location [Location] that contains latitude and longitude.
     */
    fun portal(location: Location): Single<Portal>

    /**
     * Get a [Single] which will emit a [Portal].
     *
     * @param id portal id
     */
    fun portal(id: String): Single<Portal>
}
