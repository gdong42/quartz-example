package info.donggan.blog.config;

import info.donggan.blog.job.ExampleCronJob;
import info.donggan.blog.job.ExampleRepeatJob;
import liquibase.integration.spring.SpringLiquibase;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by gdong on 16/8/25.
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfig {

  @Autowired
  @Qualifier("exampleRepeatJobTrigger")
  private Trigger repeatJobTrigger;

  @Autowired
  @Qualifier("exampleCronJobTrigger")
  private Trigger cronJobTrigger;

  @Bean
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean
        .setLocation(new ClassPathResource("/quartz.properties"));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }

  @Bean
  public JobFactory jobFactory(ApplicationContext applicationContext,
      SpringLiquibase springLiquibase) {
    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource,
      JobFactory jobFactory) throws IOException {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setOverwriteExistingJobs(true);
    factory.setDataSource(dataSource);
    factory.setJobFactory(jobFactory);
    factory.setQuartzProperties(quartzProperties());

    factory.setTriggers(repeatJobTrigger, cronJobTrigger);
    return factory;
  }

  // configure example repeat job:

  @Bean
  public JobDetailFactoryBean exampleRepeatJobDetail() {
    return createJobDetail(ExampleRepeatJob.class);
  }

  @Bean(name = "exampleRepeatJobTrigger")
  public SimpleTriggerFactoryBean exampleRepeatJobTrigger(
      @Qualifier("exampleRepeatJobDetail") JobDetail jobDetail,
      @Value("${job.example.repeat.frequency}") long frequency) {
    return createTrigger(jobDetail, frequency);
  }

  // configure example cron job:

  @Bean
  public JobDetailFactoryBean exampleCronJobDetail() {
    return createJobDetail(ExampleCronJob.class);
  }

  @Bean(name = "exampleCronJobTrigger")
  public CronTriggerFactoryBean exampleCronJobTrigger(
      @Qualifier("exampleCronJobDetail") JobDetail jobDetail,
      @Value("${job.example.cron.expression}") String expression) {
    return createCronTrigger(jobDetail, expression);
  }

  private static JobDetailFactoryBean createJobDetail(Class jobClass) {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(jobClass);
    factoryBean.setDurability(true);
    return factoryBean;
  }

  private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail,
      long pollFrequencyMs) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setStartDelay(0L);
    factoryBean.setRepeatInterval(pollFrequencyMs);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(
        SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }

  // Use this method for creating cron triggers instead of simple triggers:
  private static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail,
      String cronExpr) {
    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setCronExpression(cronExpr);
    factoryBean
        .setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
    return factoryBean;
  }
}
