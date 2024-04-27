package com.sqx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SqxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqxApplication.class, args);
		System.out.println("(♥◠‿◠)ﾉﾞ  短剧系统启动成功   ლ(´ڡ`ლ)ﾞ  \n"+
							"       _    \n" +
							"      | |   \n" +
							"  ___ | | __\n" +
							" / _ \\| |/ /\n" +
							"| (_) |   < \n" +
							" \\___/|_|\\_\\");

	}

}