package com.xiafei.tools.sftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * sftp配置.
 */
@Data
@ConfigurationProperties(prefix = "sftp.config")
public class SftpProperties {

    /**
     * 平台sftp文件服务器配置.
     */
    private Prop pt;

    /**
     * 客如云sftp文件服务器配置.
     */
    private Prop kry;

    @Data
    public static class Prop {
        private Integer port;
        private String host;
        private String userName;
        private String password;
        private Integer timeOut;
        private Integer connectionMaxSize;
        private Integer connectionInitSize;
    }

}
