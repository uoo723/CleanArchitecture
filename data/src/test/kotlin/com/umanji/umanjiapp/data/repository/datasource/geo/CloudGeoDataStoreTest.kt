package com.umanji.umanjiapp.data.repository.datasource.geo

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.umanji.umanjiapp.data.entity.PortalEntity
import com.umanji.umanjiapp.data.network.ApiResponse
import com.umanji.umanjiapp.data.network.RestApi
import com.umanji.umanjiapp.domain.entity.Location
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class CloudGeoDataStoreTest : Spek({
    lateinit var cloudGeoDataStore: CloudGeoDataStore

    val fakePortal = listOf(PortalEntity(id = "1"))
    val fakeApiResponse = ApiResponse(data = fakePortal)
    val fakeLocation = Location(0.0, 0.0)

    val mockRestApi: RestApi = mock()
    lateinit var testObserver: TestObserver<PortalEntity>

    When calling mockRestApi.portal(any(), any(), anyOrNull()) itReturns Single.just(fakeApiResponse)

    beforeEachTest {
        cloudGeoDataStore = CloudGeoDataStore(mockRestApi)
    }

    on("which CloudGeoDataStore#portal is successful") {
        testObserver = cloudGeoDataStore.portal(fakeLocation).test()

        it("must emit a PortalEntity") {
            testObserver.assertValue(fakePortal[0])
        }
    }
})
