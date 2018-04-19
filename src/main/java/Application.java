import com.xiafei.tools.nosql.redis.JedisClientProperties;
import com.xiafei.tools.sftp.SftpProperties;
import com.xiafei.tools.spring.springboot.dubbo.DubboProperties;
import com.xiafei.tools.spring.springboot.mq.RocketMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * <P>Description: springboot启动类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/1</P>
 * <P>UPDATE DATE: 2017/12/1</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@SpringBootApplication(scanBasePackages = {"com.xiafei.tools"}, exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({JedisClientProperties.class, RocketMQProperties.class, DubboProperties.class,
        SftpProperties.class})
// 如果用到定时任务配置则用上这个注解
@EnableScheduling
// 如果用到设计模式，springBean涉及到继承和实现,则配置这个注解,否则程序会报错找不到类型
@EnableCaching(proxyTargetClass = true)
public class Application extends SpringBootServletInitializer implements SchedulingConfigurer {

    static {
        /*
           解决dubbo和lockback集成后不打印日志的问题
         */
        try {
            System.setProperty("dubbo.application.logger", "slf4j");
        } catch (Throwable e) {
            System.out.println("设置dubbo默认日志接口失败" + e.getMessage());
        }
        /*
          解决java.io.IOException: Can not lock the registry cache file问题
         */
        try {
            System.setProperty("dubbo.registry.file", "/home/dubbo/cache/dubbo-registry-loan-gateway.cache");
        } catch (Throwable e) {
            System.out.println("设置dubbo缓存文件存放路径失败" + e.getMessage());
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(getClass());
    }

    @Autowired
    void setEnviroment(Environment env) {
        System.out.println("read <<from>> from env: "
                + env.getProperty("from"));
    }

    @Override
    public void configureTasks(final ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //设定一个长度10的定时任务线程池
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }
}
