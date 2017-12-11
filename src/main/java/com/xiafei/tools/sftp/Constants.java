package com.xiafei.tools.sftp;


/**
 * <P>Description: 网关系统常量. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/5</P>
 * <P>UPDATE DATE: 2017/12/5</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class Constants {

    private Constants() {

    }

    /**
     * 金额类的查不到数据给前端传的值.
     */
    public static final String AMOUNT_NODATA = "-1";

    /**
     * 字符串类型查不到数据给前端传的值.
     */
    public static final String STR_NODATA = "-";

    /**
     * sftp缓冲区的大小（8K）.
     */
    public static final int SFTP_BUFFER_SIZE = 1024 << 3;

    /**
     * sftp服务器的文件路径分隔符.
     */
//    public static final String FILE_SEPARATOR = File.separator;
    public static final String FILE_SEPARATOR = "/";
    /**
     * 用户类文件上传路径前缀.
     */
    public static final String UPLOAD_PATH_USER_PREFIX = FILE_SEPARATOR + "data" + FILE_SEPARATOR + "user" + FILE_SEPARATOR;
    /**
     * 贷款类文件上传路径前缀.
     */
    public static final String UPLOAD_PATH_LOAN_PREFIX = FILE_SEPARATOR + "data" + FILE_SEPARATOR + "loan" + FILE_SEPARATOR;

}
