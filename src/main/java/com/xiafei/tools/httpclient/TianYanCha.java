package com.xiafei.tools.httpclient;

import com.xiafei.tools.common.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * <P>Description: 天眼查. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/3/2</P>
 * <P>UPDATE DATE: 2018/3/2</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
public class TianYanCha {

    private static String token = "74a4b2c1-bd1c-4c06-a8cb-8f407e5c2d91";


    /**
     * 天眼查搜索接口.
     *
     * @param word 关键字
     */
    public static Resp353 search353(String word) throws IOException {

        HttpGet get = new HttpGet("http://open.api.tianyancha.com/services/v4/open/searchV2?word=".concat(URLEncoder.encode(word, "utf-8")));
        get.setHeader("Authorization", token);
        CloseableHttpClient client = HttpClients.createDefault();
        final CloseableHttpResponse execute = client.execute(get);
        String json = EntityUtils.toString(execute.getEntity(), "utf-8");
        log.info("search353(),word={},resp={}", word, json);
        return JsonUtil.fromJson(json, Resp353.class);
    }

    /**
     * 天眼查362查询企业基本信息接口.
     *
     * @param id 查询企业在天眼查系统内id
     */
    public static Resp362 search362(String id) throws IOException {
        HttpGet get = new HttpGet("http://open.api.tianyancha.com/services/v4/open/baseinfo?id=".concat(id));
        get.setHeader("Authorization", "74a4b2c1-bd1c-4c06-a8cb-8f407e5c2d91");
        CloseableHttpClient client = HttpClients.createDefault();
        final CloseableHttpResponse execute = client.execute(get);
        String json = EntityUtils.toString(execute.getEntity(), "utf-8");
        log.info("search362(),id={},resp={}", id, json);
        return JsonUtil.fromJson(json, Resp362.class);
    }

    @Data
    public static class RespBase<D> {
        private Integer error_code;
        private String reason;
        private D result;

    }

    /**
     * 调用353接口响应对象.
     */
    public static class Resp353 extends RespBase<Resp353.Result> {

        @Data
        public static class Result {
            /**
             * 查询结果总数.
             */
            private Integer total;
            /**
             * 查询结果对象.
             */
            private Item[] items;
        }

        @Data
        public static class Item {
            /**
             * 公司id.
             */
            private Long id;
            /**
             * 注册资本，是中文描述，例 250 万元 人民币 或 250 万元 美元等.
             */
            private String regCapital;
            /**
             * 企业名称.
             */
            private String name;
            /**
             * 企业注册省份.
             */
            private String base;
            /**
             * 企业类型.
             *
             * @see CompanyTypeEnum
             */
            private Integer companyType;

            /**
             * 开业时间 格式 YYYY-MM-dd 00:00:00
             */
            private String estiblishTime;
            /**
             * 法人姓名.
             */
            private String legalPersonName;
            /**
             * 法人类型.
             *
             * @see TypeEnum
             */
            private Integer type;
        }
    }

    public static class Resp362 extends RespBase<Resp362.Result> {

        @Data
        public static class Result {
            /**
             * 更新时间，时间戳.
             */
            private Long updateTime;
            /**
             * 经营开始时间.
             */
            private Long fromTime;
            /**
             * 经营结束时间.
             */
            private Integer categoryScore;
            /**
             * 法人类型.
             *
             * @see TypeEnum
             */
            private Integer type;

            /**
             * 公司在天眼查系统中的id.
             */
            private Long id;

            /**
             * 企业评分.
             */
            private Integer percentileScore;

            /**
             * 注册号.
             */
            private String regNumber;

            /**
             * 注册资本.
             */
            private String regCapital;

            /**
             * 公司名.
             */
            private String name;

            /**
             * 登机机关.
             */
            private String regInstitute;

            /**
             * 注册地址.
             */
            private String regLocation;

            /**
             * 行业.
             */
            private String industry;

            /**
             * 核准时间，时间戳.
             */
            private Long approvedTime;

            /**
             * 核准机构.
             */
            private String orgApprovedInstitute;

            /**
             * logo.
             */
            private String logo;

            /**
             * 纳税人识别号.
             */
            private String taxNumber;

            /**
             * 经营范围，多个之间采用英文分号分隔.
             */
            private String businessScope;
            /**
             * 纳税人识别号.
             */
            private String property4;

            /**
             * 英文名.
             */
            private String property3;

            /**
             * 组织机构代码.
             */
            private String orgNumber;

            /**
             * 开业时间，时间戳.
             */
            private Long estiblishTime;

            /**
             * 经营状态.
             */
            private String regStatus;

            /**
             * 法人id.
             */
            private Long legalPersonId;

            /**
             * 法人姓名.
             */
            private String legalPersonName;

            /**
             * 来源.
             */
            private String sourceFlag;

            /**
             * 新公司名id.
             */
            private String correctCompanyId;

            /**
             * 公司类型.
             */
            private String companyOrgType;

            /**
             * 省份拼音首字母.
             */
            private String base;

            /**
             * 社会统一信用代码.
             */
            private String creditCode;
        }
    }


    /**
     * 法人类型枚举.
     */
    public enum TypeEnum {
        PERSON(0, "人"),
        COMPANY(1, "公司");

        public final Integer code;
        public final String desc;

        TypeEnum(final Integer code, final String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

    /**
     * 企业类型枚举.
     */
    public enum CompanyTypeEnum {
        COMPANY(1, "公司"),
        HONGKONG(2, "香港公司"),
        SOCIAL_ORG(3, "社会组织"),
        LAW_OFFICE(4, "律所"),
        INSTITUTION(5, "事业单位"),
        FUND(6, "基金会");
        public final Integer code;
        public final String desc;

        CompanyTypeEnum(final Integer code, final String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
