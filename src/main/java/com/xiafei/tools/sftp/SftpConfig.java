package com.xiafei.tools.sftp;

import com.jcraft.jsch.JSchException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <P>Description: 初始化Sftp的bean. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/26</P>
 * <P>UPDATE DATE: 2017/12/26</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Configuration
public class SftpConfig {

    @Resource
    private SftpProperties sftpProperties;
//
//    @Bean
//    public Sftp sftp() throws JSchException {
//        return new Sftp(sftpProperties.getHost(), sftpProperties.getPort(), sftpProperties.getUserName(),
//                sftpProperties.getPassword(), sftpProperties.getTimeOut(), sftpProperties.getConnectionInitSize(),
//                sftpProperties.getConnectionMaxSize());
//    }
}
