package info.donggan.blog.job;

import info.donggan.blog.service.FooService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.Random;

/**
 * Date: 16/8/25
 * Time: 下午2:55
 *
 * @author Gan Dong
 */
@DisallowConcurrentExecution
public class ExampleCronJob implements Job {

  @Autowired
  private FooService fooService;

  @Override
  public void execute(JobExecutionContext context)
      throws JobExecutionException {
    int duration = new Random().nextInt(1000 * 60 * 3);
    System.out.println(LocalTime.now() +
        " -> Cron job running with instance: " + context.getFireInstanceId() +
        ": " + fooService.getFoo() + ". Duration is about " + duration);
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
