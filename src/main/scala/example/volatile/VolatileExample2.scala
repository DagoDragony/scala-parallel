package example.volatile

object VolatileExample2 {
  @volatile
  private var MY_INT = 0

  def main(args: Array[String]): Unit = {
    new VolatileExample2.ChangeListener().start()
    new VolatileExample2.ChangeMaker().start()
  }

  class ChangeListener extends Thread {
    override def run(): Unit = {
      var local_value = MY_INT
      while (local_value < 5)
        if (local_value != MY_INT) {
          println(s"Got Change for MY_INT : $MY_INT")
          local_value = MY_INT
        }
    }
  }

  class ChangeMaker extends Thread {
    override def run(): Unit = {
      var local_value = MY_INT
      while (MY_INT < 5) {
        println(s"Incrementing MY_INT to ${local_value + 1}")
        MY_INT = {
          local_value += 1
          local_value
        }
        try
          Thread.sleep(500)
        catch {
          case e: InterruptedException => e.printStackTrace()
        }
      }
    }
  }

}

// With the volatile keyword the output is :
//
// Incrementing MY_INT to 1
// Got Change for MY_INT : 1
// Incrementing MY_INT to 2
// Got Change for MY_INT : 2
// Incrementing MY_INT to 3
// Got Change for MY_INT : 3
// Incrementing MY_INT to 4
// Got Change for MY_INT : 4
// Incrementing MY_INT to 5
// Got Change for MY_INT : 5

// Without the volatile keyword the output is :

//
// Incrementing MY_INT to 1
// Incrementing MY_INT to 2
// Incrementing MY_INT to 3
// Incrementing MY_INT to 4
// Incrementing MY_INT to 5




