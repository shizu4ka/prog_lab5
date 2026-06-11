package server.threading

import java.util.concurrent.*

object ThreadPoolManager {
    // Fixed thread pool for reading requests (accepting connections)
    val readingPool: ExecutorService = Executors.newFixedThreadPool(4)

    // Cached thread pool for processing requests
    val processingPool: ExecutorService = Executors.newCachedThreadPool()

    // Fixed thread pool for sending responses
    val sendingPool: ExecutorService = Executors.newFixedThreadPool(4)

    fun shutdown() {
        readingPool.shutdown()
        processingPool.shutdown()
        sendingPool.shutdown()
    }

    fun awaitTermination(timeout: Long, unit: TimeUnit) {
        readingPool.awaitTermination(timeout, unit)
        processingPool.awaitTermination(timeout, unit)
        sendingPool.awaitTermination(timeout, unit)
    }
}