package com.umanji.umanjiapp.data.executor

import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Decorated [java.util.concurrent.ThreadPoolExecutor]
 */
@Singleton
class JobExecutor @Inject constructor() : ThreadExecutor {

    private val threadPoolExecutor = ThreadPoolExecutor(3, 5, 10,
            TimeUnit.SECONDS, LinkedBlockingDeque(), JobThreadFactory())

    override fun execute(command: Runnable) {
        threadPoolExecutor.execute(command)
    }

    private class JobThreadFactory : ThreadFactory {
        private var count = 0
        override fun newThread(r: Runnable): Thread {
            return Thread(r, "Umanji_" + count++)
        }
    }
}
