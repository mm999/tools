package com.xiafei.tools.spring;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * <P>Description: 编程式事务类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/6/7</P>
 * <P>UPDATE DATE: 2017/6/28</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
//@Component 没有数据库所以这里注掉
public class Transaction {
    @Resource
    private PlatformTransactionManager transactionManager;

    /**
     * 执行事务.
     *
     * @param task 执行任务
     * @throws Exception 可能发生的异常
     */
    public final void execute(final TransTask task) throws Exception {
        // 事物控制开始
        final DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        final TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            task.task();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * <P>Description: 编程式事务要执行的任务. </P>
     * <P>CALLED BY:   齐霞飞 </P>
     * <P>UPDATE BY:   齐霞飞 </P>
     * <P>CREATE DATE: 2017/6/7</P>
     * <P>UPDATE DATE: 2017/6/28</P>
     *
     * @author qixiafei
     * @version 1.0
     * @since java 1.7.0
     */
    public interface TransTask {
        /**
         * 事务中要执行的任务.
         */
        void task();
    }
}
