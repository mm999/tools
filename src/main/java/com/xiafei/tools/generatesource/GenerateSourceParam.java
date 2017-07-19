package com.xiafei.tools.generatesource;

import com.xiafei.tools.utils.CollectionUtils;

import java.util.List;

/**
 * <P>Description: 自动生成资源文件参数. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017年7月13日</P>
 * <P>UPDATE DATE: 2017年7月13日</P>
 *
 * @author qixiafei
 * @version 0.0.1-SNAPSHOT
 * @since java 1.7.0
 */
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
     * 是否替换原文件，若为false则在原文件内容上追加
     */
    private boolean coverFile = false;

    /**
     * 生成文件的明细信息.
     */
    private List<GenerateSourceParamItem> items;

    public String getDomainSuffix() {
        return domainSuffix;
    }

    public void setDomainSuffix(String domainSuffix) {
        this.domainSuffix = domainSuffix;
    }

    public String getDomainDirectory() {
        return domainDirectory;
    }

    public void setDomainDirectory(final String pDomainPath) {
        domainDirectory = pDomainPath;
    }

    public String getCommentsUser() {
        return commentsUser;
    }

    public void setCommentsUser(final String pCommentsUser) {
        commentsUser = pCommentsUser;
    }

    public String getCommentsSince() {
        return commentsSince;
    }

    public void setCommentsSince(final String pCommentsSince) {
        commentsSince = pCommentsSince;
    }

    public String getCommentsVersion() {
        return commentsVersion;
    }

    public void setCommentsVersion(final String pCommentsVersion) {
        commentsVersion = pCommentsVersion;
    }

    public String getDaoDirectory() {
        return daoDirectory;
    }

    public void setDaoDirectory(final String pDaoPath) {
        daoDirectory = pDaoPath;
    }

    public String getMapperDirectory() {
        return mapperDirectory;
    }

    public void setMapperDirectory(final String pMapperPath) {
        mapperDirectory = pMapperPath;
    }

    public List<GenerateSourceParamItem> getItems() {
        return items;
    }

    public void setItems(final List<GenerateSourceParamItem> pItems) {
        items = pItems;
    }

    public String getDomainPackage() {
        return domainPackage;
    }

    public void setDomainPackage(final String pDomainPackage) {
        domainPackage = pDomainPackage;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(final String pDaoPackage) {
        daoPackage = pDaoPackage;
    }

    public boolean isCoverFile() {
        return coverFile;
    }

    public void setCoverFile(final boolean coverFile) {
        this.coverFile = coverFile;
    }

    @Override
    public String toString() {
        return "GenerateSourceParam{" +
            "commentsUser='" + commentsUser + '\'' +
            ", commentsSince='" + commentsSince + '\'' +
            ", commentsVersion='" + commentsVersion + '\'' +
            ", domainDirectory='" + domainDirectory + '\'' +
            ", domainPackage='" + domainPackage + '\'' +
            ", daoDirectory='" + daoDirectory + '\'' +
            ", daoPackage='" + daoPackage + '\'' +
            ", mapperDirectory='" + mapperDirectory + '\'' +
            ", coverFile='" + coverFile + '\'' +
            ", items=" + CollectionUtils.toString(items) +
            '}';
    }
}
