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
 * <P>Description: springboot自带的定时任务就可以胜任，该类以后只作为技术尝试
 * 定时任务配置，只要放在spring扫描路径下即可，想要实现定时任务实现接口LoopTask就可以. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/6</P>
 * <P>UPDATE DATE: 2018/2/6</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Deprecated
@Slf4j
@Component
public class LoopConfig implements ApplicationContextAware {

    /**
     * 定时任务列表.
     */
    private final List<LoopTask> loopTaskList = new ArrayList<>();

    /**
     * 第一次跑定时任务距离容器启动延迟，单位ms.
     */
    private static final long INIT_DELAY = 10000L;

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
                long initDelay;
                final Long firstTime = task.firstTime();
                if (firstTime != null) {
                    initDelay = firstTime - System.currentTimeMillis();
                    if (initDelay < 0) {
                        initDelay = 0L;
                    }
                } else {
                    initDelay = INIT_DELAY;
                }
                if (task.concurrent()) {
                    ses.scheduleAtFixedRate(task::invoke, initDelay, task.period(), TimeUnit.MILLISECONDS);
                } else {
                    ses.scheduleWithFixedDelay(task::invoke, initDelay, task.period(), TimeUnit.MILLISECONDS);
                }
                log.info("{},循环定时任务启动成功，间隔={}，并发={}", task.getClass().getName(), task.period(), task.concurrent());
            }
        }
    }
}
