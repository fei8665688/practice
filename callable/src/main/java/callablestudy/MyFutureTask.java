package callablestudy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author phil.zhang
 * @date 2019/3/5
 */
public class MyFutureTask  {

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, TimeoutException {

    MyCallable callable1 = new MyCallable();
    MyCallable callable2 = new MyCallable();

    FutureTask<String> futureTask1 = new FutureTask<>(callable1);
    FutureTask<String> futureTask2 = new FutureTask<>(callable2);

    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, 1000,
        TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10));

    poolExecutor.execute(futureTask1);
    poolExecutor.execute(futureTask2);

    while (true) {
      try {
        System.out.println("task1:"+futureTask1.get(10L, TimeUnit.MILLISECONDS));
        System.out.println("task2:"+futureTask2.get(10L, TimeUnit.MILLISECONDS));
      }catch (Exception e) {

      }
      System.out.println(Thread.currentThread().getName());
      if (null != futureTask1.get() && null != futureTask2.get()) {
        poolExecutor.shutdown();
        break;
      }
    }

  }

}
