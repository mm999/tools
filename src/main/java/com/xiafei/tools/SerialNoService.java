package com.xiafei.tools;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <P>Description: 生成序列号服务.
 * 序列号组成：yyyyMMddHHmmssSSS + 四位模块编码 + 5位流水号，可以支持每毫秒9万9千9百9十9次请求
 * 注：单机版，若大型分布式系统需要使用另一个工具</P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/3/6</P>
 * <P>UPDATE DATE: 2018/3/6</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Service
public class SerialNoService {
    private static final FastDateFormat SDF = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
    // 当前流水号，初始化为0因为获取时候会先加一再获取.
    private static AtomicInteger currentNo = new AtomicInteger(0);
    // 最大流水号，超过这个数将继续从1开始.
    private static final int MAX = 99999;

    /**
     * 获取序列号.
     *
     * @param module 模块枚举值
     * @return 序列号
     */
    public String getNo(ModuleEnum module) {
        return SDF.format(System.currentTimeMillis()).concat(module.code).concat(getLastFive());
    }

    /**
     * 生成最后五位序号.
     *
     * @return
     */
    private String getLastFive() {
        int no = currentNo.incrementAndGet();
        if (no > MAX) {
            if (currentNo.compareAndSet(no, 1)) {
                no = 1;
            } else {
                no = currentNo.incrementAndGet();
            }
        }
        return String.format("%05d", no);
    }


    /**
     * 模块枚举.
     */
    public enum ModuleEnum {

        APPLY("0001", "租赁申请");

        public final String code;
        public final String desc;

        ModuleEnum(final String code, final String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


}
