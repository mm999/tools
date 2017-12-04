package com.xiafei.tools.generatesource;

import lombok.Data;

import java.util.List;

/**
 * <P>Description: 自动生成资源文件参数. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017年7月13日</P>
 * <P>UPDATE DATE: 2017年7月13日</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Data
public class GenerateSourceParam {

    /**
     * 类注释的用户名.
     */
    private String commentsUser;

    /**
     * 类注释的since-通常是jdk版本.
     */
    private String commentsSince;

    /**
     * 类注释的version.
     */
    private String commentsVersion;

    /**
     * domain文件生成目录地址，留空代表不生成.
     */
    private String domainDirectory;

    /**
     * domain包地址.
     */
    private String domainPackage;

    /**
     * 习惯domain类名字后缀，比如PO，可以为空
     */
    private String domainSuffix;

    /**
     * dao文件生成目录地址，留空代表不生成.
     */
    private String daoDirectory;

    /**
     * dao文件报地址.
     */
    private String daoPackage;

    /**
     * mapper文件生成目录地址，留空代表不生成.
     */
    private String mapperDirectory;

    /**
     * 是否替换原文件，若为false则在原文件内容上追加.
     */
    private boolean coverFile;

    /**
     * 是否使用java.time包的日期组件.
     */
    private boolean javaTime;

    /**
     * 是否使用了lombok插件.
     */
    private boolean lombok;

    /**
     * 生成文件的明细信息.
     */
    private List<GenerateSourceParamItem> items;

    {
        coverFile = false;
        javaTime = false;
        lombok = false;
    }
}
