package concurrent

import java.util.concurrent.ForkJoinPool

/**
  * sleep 会阻塞线程池，大量计算的任务也会阻塞线程池。
  * 按任务类型分离到各自的线程池比较好
  */
object MyForkJoinPool extends App {
  val pool = new ForkJoinPool(4)
  pool.execute(() => {
    println("sleep 1 thread")
    Thread.sleep(1000 * 10)
    println("awake 1 thread")

  })
  Thread.sleep(1000)

  pool.execute(() => {
    println("sleep 2 thread")
    Thread.sleep(1000 * 10)
    println("awake 2 thread")

  })
  Thread.sleep(1000)

  pool.execute(() => {
    println("sleep 3 thread")
    Thread.sleep(1000 * 10)
    println("awake 3 thread")

  })
  Thread.sleep(1000)

  pool.execute(() => {
    println("sleep 4 thread")
    Thread.sleep(1000 * 10)
    println("awake 4 thread")
  })


  Thread.sleep(1000)

  // execute after 10s
  pool.execute(() => {
    println("my work")
  })


  Thread.currentThread().join()

}
