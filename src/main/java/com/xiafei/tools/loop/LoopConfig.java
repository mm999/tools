package com.xiafei.tools.loop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <P>Description: 定时任务配置，会自动扫描实现了LoopTask接口的bean. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/6</P>
 * <P>UPDATE DATE: 2018/2/6</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
@Component
public class LoopConfig implements ApplicationContextAware {

    /**
     * 定时任务列表.
     */
    private final List<LoopTask> loopTaskList = new ArrayList<>();


    /**
     * 当spring容器加载完成后初始化定时任务.
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        final Map<String, LoopTask> beansOfType = applicationContext.getBeansOfType(LoopTask.class);
        if (beansOfType != null) {
            for (Map.Entry<String, LoopTask> e : beansOfType.entrySet()) {
                loopTaskList.add(e.getValue());
            }

            final ScheduledExecutorService ses = Executors.newScheduledThreadPool(loopTaskList.size());
            for (LoopTask task : loopTaskList) {
                if (task.concurrent()) {
                    ses.scheduleAtFixedRate(task::invoke, 1000L, task.delay(), TimeUnit.MILLISECONDS);
                } else {
                    ses.scheduleWithFixedDelay(task::invoke, 1000L, task.delay(), TimeUnit.MILLISECONDS);
                }
                log.info("{},循环定时任务启动成功，间隔={}，并发={}", task.getClass().getName(), task.delay(), task.concurrent());
            }
        }
    }
}
