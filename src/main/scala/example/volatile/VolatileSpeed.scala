package example.volatile

class VolatileSpeed {
  private var a: Int = 0
// uncomment those to move b to another cache 3 line to stop partial blocking
// private val dummy1: Long = 0
// private val dummy2: Long = 0
// private val dummy3: Long = 0
// private val dummy4: Long = 0
// private val dummy5: Long = 0
// private val dummy6: Long = 0
// private val dummy7: Long = 0
// private val dummy8: Long = 0
  private var b: Int = 0
}

object VolatileSpeed {
  private var lastA: Long = 0
  private var lastB: Long = 0

  def main(args: Array[String]): Unit = {
    val instance = new VolatileSpeed()
    new Thread(new Runnable() {

      override def run(): Unit = {
        lastA = System.nanoTime
        while (true) {
          instance.a += 1
          if (instance.a % 100000000 == 0) {
            System.out.println("A: " + (System.nanoTime - lastA) / 1000000 + "ms")
            lastA = System.nanoTime()
            instance.a = 0
          }
        }
      }
    }).start()

    new Thread(new Runnable() {
      override def run(): Unit = {
        lastB = System.nanoTime()
        while (true) {
          instance.b += 1
          if (instance.b % 100000000 == 0) {
            System.out.println("B: " + (System.nanoTime - lastB) / 1000000 + "ms")
            lastB = System.nanoTime()
            instance.b = 0
          }
        }
      }
    }).start()
  }
}

// case 1 when a variable volatile it takes multiple time more time (from ~ 300ms to 1000ms
// case 2 when b variable is volatile too, it takes up to ~3300 each
// ints a and b are 4 bytes long each(total 8), cpu reaches cache not byte by byte
// but 1 cache line, if we force cpu to acces l3 cache each read and write always blocks as CPU needs to ensure, that no other cpu accesses the same cache line once it performs update on it. in current case CPU cache line is 64(varies on architecture)these 2 variables fits into that 1 line. So it's take much longer to reach data. if we fill  dummy vars after a that puches b to other line, it will not block and execute as fast as 1 volatile value
