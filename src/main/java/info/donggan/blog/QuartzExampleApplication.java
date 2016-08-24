package info.donggan.blog;

import info.donggan.blog.config.SchedulerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SchedulerConfig.class})
public class QuartzExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzExampleApplication.class, args);
	}
}
