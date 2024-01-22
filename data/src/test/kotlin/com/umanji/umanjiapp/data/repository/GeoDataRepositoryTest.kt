package com.umanji.umanjiapp.data.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.umanji.umanjiapp.data.entity.PortalEntity
import com.umanji.umanjiapp.data.repository.datasource.geo.CloudGeoDataStore
import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Portal
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class GeoDataRepositoryTest : Spek({
    lateinit var geoDataRepository: GeoDataRepository

    val fakePortalEntity = PortalEntity(id = "1")
    val fakeLocation = Location(0.0, 0.0)

    val mockCloudGeoDataStore: CloudGeoDataStore = mock()
    lateinit var testObserver: TestObserver<Portal>

    When calling mockCloudGeoDataStore.portal(any()) itReturns Single.just(fakePortalEntity)

    beforeEachTest {
        geoDataRepository = GeoDataRepository(mockCloudGeoDataStore)
    }

    on("which GeoDataRepository#port is successful") {
        testObserver = geoDataRepository.portal(fakeLocation).test()

        it("must emit a Portal") {
            testObserver.assertValue { it.id == fakePortalEntity.id }
        }
    }
})
