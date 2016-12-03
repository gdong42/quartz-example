package info.donggan.blog.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Date: 02/12/2016
 * Time: 10:55 AM
 *
 * @author Gan Dong
 */
public class TimerJobListener implements JobListener {

  private static final Logger logger = LoggerFactory
      .getLogger(JobListener.class);

  @Override
  public String getName() {
    return "[Job Stopwatch]";
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext context) {
    logger.info("Job [{}] is about to start...",
        context.getJobDetail().getJobClass().getSimpleName());
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext context) {
    logger.warn("Job [{}] is about to cancel...",
        context.getJobDetail().getJobClass().getSimpleName());
  }

  @Override
  public void jobWasExecuted(JobExecutionContext context,
      JobExecutionException jobException) {
    logger.warn("Job [{}] ended, took duration {}...",
        context.getJobDetail().getJobClass().getSimpleName(),
        format(context.getJobRunTime()));
  }

  private static String format(long millis) {
    return String.format("%d min, %d sec",
        TimeUnit.MILLISECONDS.toMinutes(millis),
        TimeUnit.MILLISECONDS.toSeconds(millis) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
    );
  }
}
