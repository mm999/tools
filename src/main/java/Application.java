import com.xiafei.tools.nosql.redis.JedisClientProperties;
import com.xiafei.tools.sftp.SftpProperties;
import com.xiafei.tools.springboot.dubbo.DubboProperties;
import com.xiafei.tools.springboot.mq.RocketMQProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

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
public class Application extends SpringBootServletInitializer {

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

}
