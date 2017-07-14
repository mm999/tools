package com.xiafei.tools.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * <P>Description: 编程式事务类 </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/6/7</P>
 * <P>UPDATE DATE: 2017/6/28</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
@Component
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
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new Exception(e.getMessage());
        }
        transactionManager.commit(status);
    }

    /**
     * <P>Description: 编程式事务要执行的任务. </P>
     * <P>CALLED BY:   齐霞飞 </P>
     * <P>UPDATE BY:   齐霞飞 </P>
     * <P>CREATE DATE: 2017/6/7</P>
     * <P>UPDATE DATE: 2017/6/28</P>
     *
     * @author qixiafei
     * @version 0.0.3-SNAPSHOT
     * @since java 1.7.0
     */
    public interface TransTask {
        /**
         * 事务中要执行的任务.
         */
        void task();
    }
}
