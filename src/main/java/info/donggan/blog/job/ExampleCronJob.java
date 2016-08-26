package info.donggan.blog.job;

import info.donggan.blog.service.FooService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

/**
 * Date: 16/8/25
 * Time: 下午2:55
 *
 * @author Gan Dong
 */
public class ExampleCronJob implements Job {

  @Autowired
  private FooService fooService;

  @Override
  public void execute(JobExecutionContext context)
      throws JobExecutionException {
    System.out.println(LocalTime.now() +
        " -> Cron job running with instance: " + context.getFireInstanceId() +
        ": " + fooService.getFoo());
  }
}
