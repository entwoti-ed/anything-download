package top.cyblogs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.cyblogs.init.DownloadFFMpeg;

/**
 * 程序启动类
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        DownloadFFMpeg.download();
        SpringApplication.run(Application.class, args);
    }
}
