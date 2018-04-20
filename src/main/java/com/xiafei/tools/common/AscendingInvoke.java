package com.xiafei.tools.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <P>Description: 时间递增执行工具.
 * 使用时每一个要调用的任务都有一个具体的实现类，这样初始化的时候才能找到class，不能使用匿名内部类</P>
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
public class AscendingInvoke implements ApplicationContextAware {

    /**
     * 延迟调用递增最大等级.
     */
    private static final int MAX_LEVEL = 8;

    /**
     * 保存线程私有变量和执行对象的文件.
     */
    private static final String TEMP_FILE_PATH = "/data/LOGS/tools/ascendingTemp/";

    /**
     * 运行信息分隔符.
     */
    private static final String RUNINFO_SPLITE = ",";

    /**
     * 保存字段信息保存文件后缀名.
     */
    private static final String FIELDS_FILE_SUFFIX = ".fields";

    /**
     * 保存对象序列化信息保存文件后缀名.
     */
    private static final String OBJECT_FILE_SUFFIX = ".obj";

    /**
     * 线程私有uuid.
     */
    private static final ThreadLocal<String> THREAD_UUID = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    /**
     * 每个线程已经执行过的调用等级.
     */
    private static final ThreadLocal<Integer> THREAD_LEVEL =
            ThreadLocal.withInitial(() -> 1);

    /**
     * 每个线程独立计算下一次轮询时间.
     */
    private static final ThreadLocal<Long> THREAD_DELAY =
            ThreadLocal.withInitial(() -> TimeUnit.SECONDS.toMillis(1));

    public static void main(String[] args) {
//        init();

    }


    /**
     * 开始递增调用.
     *
     * @param task 调用执行的具体任务
     */
    public static void start(final Task task) {
        new Thread(() -> {
            doIt(task);
            delRunInfo();
        }).start();
    }


    /**
     * 开始递增调用.
     *
     * @param task 调用执行的具体任务
     */
    private static void doIt(final Task task) {
        saveRunInfo(task);
        try {
            task.invoke();
        } catch (Exception e) {
            final Integer level = THREAD_LEVEL.get();
            long curDelay = THREAD_DELAY.get();
            double curDelayMinutes = curDelay / 60000.0;
            if (level == MAX_LEVEL) {
                log.error("时间递增调用任务已经达到最大时间间隔{}分钟、最大尝试次数{}次，放弃该条回调任务，任务数据={}",
                        curDelayMinutes, MAX_LEVEL, task.desc(), e);
                THREAD_LEVEL.remove();
                THREAD_DELAY.remove();
                return;
            }
            long nextDelay = curDelay << 1;
            double nextDelayMinutes = nextDelay / 60000.0;
            log.warn("递增调用任务执行失败，增加时间间隔，当前时间间隔{}分钟，增加后时间间隔{}分钟，当前第{}次尝试，一共会尝试{}次，"
                    + "任务数据={}", curDelayMinutes, nextDelayMinutes, level, MAX_LEVEL, task.desc(), e);
            THREAD_LEVEL.set(level + 1);
            THREAD_DELAY.set(nextDelay);
            try {
                Thread.sleep(nextDelay);
            } catch (InterruptedException e1) {
                log.error("线程收到中断请求，响应中断，任务结束，任务数据={}", task.desc(), e);
                return;
            }
            doIt(task);
        }
    }

    /**
     * 保存当前的执行数据.
     */
    private static void saveRunInfo(final Task task) {
        final File file = new File(TEMP_FILE_PATH.concat(THREAD_UUID.get()).concat(OBJECT_FILE_SUFFIX));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(task);
            final StringBuilder sb = new StringBuilder();
            sb.append(THREAD_LEVEL.get()).append(RUNINFO_SPLITE);
            sb.append(THREAD_DELAY.get()).append(RUNINFO_SPLITE);
            sb.append(THREAD_UUID.get());
            FileUtils.outPutToFileByLine(new File(TEMP_FILE_PATH.concat(THREAD_UUID.get()).concat(FIELDS_FILE_SUFFIX)),
                    Collections.singletonList(sb.toString()), true);
        } catch (IOException e) {
            log.warn("递增调用序列化对象到文件时任务执行过程中包含不能序列化的内容，放弃保存状态", e);
        }
    }

    /**
     * 删除执行数据.
     */
    private static void delRunInfo() {
        new File(TEMP_FILE_PATH.concat(THREAD_UUID.get()).concat(FIELDS_FILE_SUFFIX)).delete();
        new File(TEMP_FILE_PATH.concat(THREAD_UUID.get()).concat(OBJECT_FILE_SUFFIX)).delete();
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        init();
    }

    /**
     * 将由于程序终止导致停止的线程恢复运行.
     */
    private void init() {
        try {
            final String threadLevelKey = "THREAD_LEVEL";
            final String threadDelayKey = "THREAD_DELAY";
            final String threadUUID = "THREAD_UUID";
            final String task = "TASK";
            final File dirctory = new File(TEMP_FILE_PATH);
            if (!dirctory.isDirectory()) {
                dirctory.mkdirs();
                return;
            }
            final File[] files = dirctory.listFiles();
            if (files == null) {
                return;
            }
            final List<Map<String, Object>> runInfoMapList = new ArrayList<>(files.length >>> 1);
            final Map<String, Map<String, Object>> cacheMap = new HashMap<>();
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
                final String uuid = file.getName().substring(0, file.getName().lastIndexOf("."));
                final Map<String, Object> runInfoMap;
                if (cacheMap.get(uuid) != null) {
                    runInfoMap = cacheMap.get(uuid);
                } else {
                    runInfoMap = new HashMap<>();
                    cacheMap.put(uuid, runInfoMap);
                    runInfoMapList.add(runInfoMap);
                }
                if (file.getName().endsWith(FIELDS_FILE_SUFFIX)) {
                    final List<String> runInfoList = FileUtils.readFileToString(file);
                    if (runInfoList == null) {
                        log.warn("文件{}，内容为空", file.getAbsolutePath());
                        continue;
                    }
                    final String runInfo = runInfoList.get(0);
                    runInfoMap.put(threadLevelKey, Integer.parseInt(runInfo.split(RUNINFO_SPLITE)[0]));
                    runInfoMap.put(threadDelayKey, Long.parseLong(runInfo.split(RUNINFO_SPLITE)[1]));
                    runInfoMap.put(threadUUID, runInfo.split(RUNINFO_SPLITE)[2]);

                } else {
                    try (FileInputStream fis = new FileInputStream(file);
                         ObjectInputStream ois = new ObjectInputStream(fis)) {
                        final Object o = ois.readObject();
                        ois.close();
                        runInfoMap.put(task, o);

                    }

                }
            }

            for (Map<String, Object> runInfoMap : runInfoMapList) {
                new Thread(() -> {
                    THREAD_LEVEL.set((Integer) runInfoMap.get(threadLevelKey));
                    THREAD_DELAY.set((Long) runInfoMap.get(threadDelayKey));
                    THREAD_UUID.set((String) runInfoMap.get(threadUUID));
                    doIt((Task) runInfoMap.get(task));
                    delRunInfo();
                }).start();
            }
        } catch (Exception e) {
            log.error("读取递增调用持久化对象失败", e);
        }

    }

    /**
     * 递增调用任务.
     */
    public interface Task extends Serializable {

        /**
         * 执行回调任务，如果回调失败一定要抛出异常.
         */
        void invoke() throws Exception;

        /**
         * 返回任务描述，用于记录日志.
         */
        String desc();
    }

}
