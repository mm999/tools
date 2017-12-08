package com.xiafei.tools.utils;

import com.xiafei.tools.exceptions.BizException;
import com.xiafei.tools.retry.Message;
import com.xiafei.tools.retry.Messages;
import org.slf4j.Logger;

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
public class DubboInvokeUtils {

    private DubboInvokeUtils() {

    }

    /**
     * 调用Dubbo接口并且返回数据.
     *
     * @param task          接口调用任务
     * @param param         调用dubbo接口参数
     * @param log           日志记录接口
     * @param userAccountId 用户id（可为空）
     * @param uuid          业务线唯一id（可为空）
     * @param methodDesc    方法描述
     * @param interfaceDesc 接口描述
     * @param <D>           dubbo接口返回Message中包含的data泛型
     * @param <I>           dubbo接口调用参数泛型
     * @return dubbo接口调用返回结果message中的data或抛出bizException
     */
    public static <D, I> D getData(DubboTask<D, I> task, I param, Logger log, String userAccountId,
                                              String uuid, String methodDesc, String interfaceDesc) {
        log.info("{}[{}],{},invoke{},param={}", userAccountId, uuid, methodDesc, interfaceDesc, param);
        final Message<D> msg = task.invoke(param);
        log.info("{}[{}],{},invoke{},resp={}", userAccountId, uuid, methodDesc, interfaceDesc, msg);
        if (Messages.isSuccess(msg)) {
            return msg.getData();
        } else {
            log.error("{}[{}],{},invoke{} failed,msg={}", userAccountId, uuid, methodDesc, interfaceDesc, msg);
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
