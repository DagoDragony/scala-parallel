package example.volatile

object VolatileExample1 extends App {
  // this part is copied to core cache
  // however, it is changed only for one cache
  // keyword volatile would make this value to copy to L3
  // cache that is shared between all cores
  // without sharing between cores thread is stuck in infinite loop
  private var running = false

  new Thread(() => {
    // Wait for running to become true
    while (!running) {}

    System.out.println("Started.");

    // Wait for running to become false
    while (running) {}

    System.out.println("Stopped.");
  }).start();

  // Wait one second
  Thread.sleep(1000);
  System.out.println("Starting.");

  // Set running to true
  running = true;

  // Wait one second
  Thread.sleep(1000);
  System.out.println("Stopping.");

  // Set running to false
  running = false
}