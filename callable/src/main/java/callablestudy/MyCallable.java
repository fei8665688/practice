package callablestudy;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author phil.zhang
 * @date 2019/3/5
 */
public class MyCallable implements Callable<String> {

  @Override
  public String call() throws Exception {
    Thread.sleep(1000);
    System.out.println(Thread.currentThread().getName());
    return "test";
  }
}
