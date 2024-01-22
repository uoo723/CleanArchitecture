package com.umanji.umanjiapp.domain.interactor

import com.nhaarman.mockitokotlin2.mock
import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Portal
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.GeoRepository
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class GetPortalTest : Spek({
    lateinit var getPortal: GetPortal

    val fakePortal = Portal(id = "1")
    val fakeParams = GetPortal.Params(location = Location(37.519709, 126.9388314))

    val mockThreadExecutor: ThreadExecutor = mock()
    val mockPostExecutionThread: PostExecutionThread = mock()
    val mockGeoRepository: GeoRepository = mock()
    lateinit var testObserver: TestObserver<Portal>

    When calling mockGeoRepository.portal(fakeParams.location!!) itReturns Single.just(fakePortal)

    beforeEachTest {
        getPortal = GetPortal(mockThreadExecutor, mockPostExecutionThread, mockGeoRepository)
    }

    on("which portal info is existed") {
        testObserver = getPortal.buildUseCaseObservable(fakeParams).test()

        it("must emit a Portal") {
            testObserver.assertValue(fakePortal)
        }
    }
})
