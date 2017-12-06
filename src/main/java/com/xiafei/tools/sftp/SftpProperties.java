package com.xiafei.tools.sftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * sftp配置.
 */
@Data
@ConfigurationProperties(prefix = "sftp.config")
public class SftpProperties {
    private String host;
    private Integer port;
    private String userName;
    private String password;
}
