package com.xiafei.tools.dubbo;

import com.xiafei.tools.data.Message;
import com.xiafei.tools.data.Messages;
import com.xiafei.tools.exceptions.BizException;
import lombok.extern.slf4j.Slf4j;

/**
 * <P>Description: Dubbo接口调用工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/8</P>
 * <P>UPDATE DATE: 2017/12/8</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
public class DubboInvokeUtils {

    private DubboInvokeUtils() {

    }

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        getData((DubboTask<Object, Object>) Messages::success, "123", "调用userInterface.getUser()接口");
        getData((DubboTask<Object, Object>) Messages::success, "param", "");
    }

    /**
     * 调用Dubbo接口并且返回数据.
     *
     * @param task      接口调用任务
     * @param param     调用dubbo接口参数
     * @param logPrefix 日志记录前缀，通常包含 用户id,业务线唯一id,invoke 接口名,
     *                  例：123[ddaasdfsdf],invoke userInterface.getUser()
     * @param <D>       dubbo接口返回Message中包含的data泛型
     * @param <I>       dubbo接口调用参数泛型
     * @return dubbo接口调用返回结果message中的data或抛出bizException
     */
    public static <D, I> D getData(DubboTask<D, I> task, I param, final String logPrefix) {
        // 方法调用着stack信息
        final String methodInvoker = Thread.currentThread().getStackTrace()[2].toString();
        log.info("{}{},param={}", methodInvoker, logPrefix, param);
        final Message<D> msg = task.invoke(param);
        log.info("{}{},resp={}", methodInvoker, logPrefix, msg);
        if (Messages.isSuccess(msg)) {
            return msg.getData();
        } else {
            log.error("{}{},failed,msg={}", methodInvoker, logPrefix, msg);
            throw new BizException(msg.getCode(), msg.getMessage());
        }
    }

    /**
     * dubbo接口调用任务内部类.
     *
     * @param <D> 返回Mesage中的data泛型
     * @param <I> 调用dubbo接口入参泛型
     */
    public interface DubboTask<D, I> {
        Message<D> invoke(I param);
    }
}
