package blob.volgo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@SpringBootApplication(scanBasePackages = {"blob.volgo"})
@EnableScheduling
public class ElasticBeanshellApplicationV1 implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(ElasticBeanshellApplicationV1.class);

	@Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }	
    public static void main(String[] args)
    {
        SpringApplication.run(ElasticBeanshellApplicationV1.class, args);
        log.info("Started application...");
    }
    
	public void run(String... args) throws Exception {
		log.info("args = "+args);
		
	}
}
