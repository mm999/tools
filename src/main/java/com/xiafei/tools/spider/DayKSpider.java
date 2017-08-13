package com.xiafei.tools.spider;

import com.xiafei.tools.utils.FileUtils;
import com.xiafei.tools.utils.MapUtils;
import com.xiafei.tools.utils.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <P>Description: 上海黄金交易所日交易行情抓取. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/13</P>
 * <P>UPDATE DATE: 2017/8/13</P>
 *
 * @author qixiafei
 * @version 0.0.1-SNAPSHOT
 * @since java 1.7.0
 */
public class DayKSpider {


    public static void main(String[] args) {
        generalDayKSQL();

    }

    /**
     * 分析交易所页面生成日K分析结果，509612这个序号的页面有毒，自己看.
     */
    private static void generalDayKSQL() {
        NoLoginPageGetter getter = new NoLoginPageGetter();
        // 抓取分析出的日K数据列表，每一项对应日报表格一行数据
        final List<DayK> dayKData = new ArrayList<>();
        String url = null;

        try (FileInputStream in = new FileInputStream("C:\\work\\tools\\src\\main\\java\\com\\xiafei\\tools\\spider\\effectIndex.txt");
             InputStreamReader ir = new InputStreamReader(in, "utf-8");
             BufferedReader br = new BufferedReader(ir)) {
            // 从文件中读取有效页面编号
            final String effectNos = br.readLine();
            final String[] effectNoArray = effectNos.split(",");
            for (String pageNo : effectNoArray) {
                url = "https://www.sge.com.cn/sjzx/mrhqsj/".concat(pageNo).concat("?top=789398439266459648");
                System.out.println("当前页码：" + pageNo);
                // 获取网页文档对象
                Document document = getter.getPageJsoup(url);

                final Map<String, Boolean> statusParam = MapUtils.newHashMap(1);
                List<DayK> pageDayK = analysisDocument(document, statusParam, url);
                if (statusParam.get("continue") != null && statusParam.get("continue")) {
                    continue;
                }
                if (pageDayK != null && !pageDayK.isEmpty()) {
                    dayKData.addAll(pageDayK);
                }
//                try {
//                    // 反爬虫屏蔽
//                    Thread.sleep((long) Math.random() << 10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        } catch (Exception e) {
            System.out.println("未处理异常，网址：" + url);
            e.printStackTrace();
            return;
        }
        System.out.println("总页面数量：" + dayKData.size());
        List<String> contentList = new ArrayList<>(dayKData.size());
        for (DayK k : dayKData) {
            contentList.add(k.toString());
            System.out.println(k);
        }
        FileUtils.outPutToFileByLine("C:\\work\\tools\\src\\main\\java\\com\\xiafei\\tools\\spider\\dayk.txt", contentList, true);

    }

    /**
     * 分析页面的Document生成日K数据列表.
     *
     * @param document    页面文档对象
     * @param statusParam 状态控制对象
     * @return 当前页面的日K列表
     */
    private static List<DayK> analysisDocument(final Document document, final Map<String, Boolean> statusParam, final String url) {
        final List<DayK> currentPageDayKList = new ArrayList<>();

        if (!document.toString().contains("每日行情详情")) {
            statusParam.put("continue", true);
            return null;
        }
        // 页面标题信息，拿到交易日期
        final Element pageTitle = document.select("div.jzk_newsCenter_meeting").get(0).child(0);
        final Integer timeDay = Integer.parseInt(pageTitle.select("span").get(1).html().
            replace("<i>时间:</i>", "").replace("\"", "").
            replace("-", ""));

        final Elements tables = document.select("table");
        // 表格的行
        final Elements trs;
        if (tables.size() > 0) {
            trs = tables.get(tables.size() - 1).getElementsByTag("tr");
            if (trs.size() == 0) {
                statusParam.put("continue", true);
                return null;
            }
            // 去掉页脚的内容
            for (int j = 0; j < trs.size(); j++) {
                if (trs.get(j).parent().tagName().equals("tfoot")) {
                    trs.remove(j);
                }
            }
        } else {
            statusParam.put("continue", true);
            return null;
        }

        // 循环表头单元格，根据单元格特征确认列类型列表
        final Element titleTr = trs.get(0);
        final List<ColumnTypeEnum> cTypeList = new ArrayList<>(15);
        final Elements titleTds = titleTr.select("th").size() == 0
            ? titleTr.select("td")
            : titleTr.select("th");
        cTypeList.add(ColumnTypeEnum.INST_ID);// 第一列不校验了，就当成合约代码
        titleTds.remove(0);
        for (Element td : titleTds) {
            final String headerTdStr = td.html();
            ColumnTypeEnum columnTypeEnum = judgeColumnType(headerTdStr);
            if (columnTypeEnum != null) {
                cTypeList.add(columnTypeEnum);
            }
        }

        // 载入表格数据
        for (int j = 1, len = trs.size(); j < len; j++) {
            // 循环每一行时对应一个日K
            final DayK item = newDayK();
            final Elements tds = trs.get(j).select("td");
            item.setTimeDay(timeDay);
            item.setUrl(url);
            // 循环每一列对应日K一个属性
            for (int z = 0, lenz = tds.size(); z < lenz && z < cTypeList.size(); z++) {
                final Element td = tds.get(z);

                String value = "";

                final Elements spans = td.select("span[lang=EN-US]");
                if (spans.size() > 0) {
                    for (Element span : spans) {
                        if (span.children().size() > 0) {
                            value += span.child(0).html();
                        } else {
                            value += span.html();
                        }
                    }
                } else if (td.select("div.MsoNormal").size() > 0) {
                    value = td.select("div.MsoNormal").select("font").html();
                } else if (td.select("p.MsoNormal").size() > 0) {
                    value = td.select("p.MsoNormal").select("span").html();
                } else if (td.select("span").size() == 1) {
                    value = td.select("span").get(0).html();
                }

                if (StringUtils.isBlank(value)) {
                    for (Element child : td.children()) {
                        if (child.tagName().equals("script")) {
                            child.remove();
                        }
                    }
                    if (td.children().size() > 0) {
                        value = td.child(0).html();
                    } else {
                        value = td.html();
                    }
                }


                try {
                    value = value.replace("&nbsp;", "");
                    setProperty(value, cTypeList.get(z), item);
                } catch (Exception e) {
                    System.out.println("value[" + value + "]，td[" + td.html() + "]，标题：[" + cTypeList.get(z) +
                        "]，标题列表[" + cTypeList + "]，第一个tr[" + titleTr + "]");
                    throw e;
                }

            }
            currentPageDayKList.add(item);
        }

        return currentPageDayKList;
    }

    /**
     * 判断列类型.
     *
     * @param headerTdStr 表头一个单元格的html内容
     * @return 列的类型
     */
    private static ColumnTypeEnum judgeColumnType(final String headerTdStr) {
        if (headerTdStr.contains("开盘")
            || headerTdStr.contains("开盘价")
            || (headerTdStr.contains("Open") && !headerTdStr.contains("Interest"))) {
            return ColumnTypeEnum.OPEN;
        } else if ((headerTdStr.contains("收") && headerTdStr.contains("盘"))
            || headerTdStr.contains("Close")) {
            return ColumnTypeEnum.CLOSE;
        } else if ((headerTdStr.contains("最") && headerTdStr.contains("高"))
            || headerTdStr.contains("High")) {
            return ColumnTypeEnum.HIGH;
        } else if ((headerTdStr.contains("最") && headerTdStr.contains("低"))
            || headerTdStr.contains("Low")) {
            return ColumnTypeEnum.LOW;
        } else if ((headerTdStr.contains("涨") && headerTdStr.contains("跌") && !headerTdStr.contains("幅"))
            || headerTdStr.contains("Up/Down(yuan)")) {
            return ColumnTypeEnum.UP_DOWN;
        } else if (headerTdStr.contains("涨") && headerTdStr.contains("跌") && headerTdStr.contains("幅")) {
            return ColumnTypeEnum.UP_DOWN_RATE;
        } else if ((headerTdStr.contains("均") && headerTdStr.contains("价"))
            || headerTdStr.contains("平均")
            || headerTdStr.contains("Weighted &nbsp; Average Price")) {
            return ColumnTypeEnum.AVERAGE;
        } else if (headerTdStr.contains("交易量")
            || (headerTdStr.contains("成") && headerTdStr.contains("交") && headerTdStr.contains("量"))
            || headerTdStr.contains("Volume(Kg)")) {
            return ColumnTypeEnum.VOLUME;
        } else if (headerTdStr.contains("交易金额")
            || (headerTdStr.contains("成") && headerTdStr.contains("交") && headerTdStr.contains("额"))
            || headerTdStr.contains("Amount(yuan)")) {
            return ColumnTypeEnum.TRUN_OVER;
        } else if (headerTdStr.contains("持仓量")
            || headerTdStr.contains("交收")
            || headerTdStr.contains("Open Interest")
            || headerTdStr.contains("Direction")
            || headerTdStr.contains("Delivery &nbsp; Volume")) {
            return null;
        } else {
            throw new IllegalArgumentException("未识别标题 --》" + headerTdStr);
        }
    }


    /**
     * 设置日K的属性.
     *
     * @param value      属性的字符串类型值
     * @param headerType 表头（列）类型
     * @param dayK       日K对象
     */
    private static void setProperty(String value, ColumnTypeEnum headerType, DayK dayK) {
        if (ColumnTypeEnum.INST_ID == headerType) {
            value = value.replace(".", "");
            if (value.equals("AuT+D")) {
                value = "Au(T+D)";
            } else if (value.contains("u(T+D)")) {
                value = "Au(T+D)";
            } else if (value.contains("g(T+D)")) {
                value = "Ag(T+D)";
            } else if (value.equals("g")) {
                value = "Au100g";
            }

            dayK.setInstId(value);
            return;
        }
        String digitOnly = StringUtils.numberOnly(value);
        if (digitOnly.equals("-") || StringUtils.isBlank(digitOnly)) {
            digitOnly = "0";
        }
        System.out.println("只留下数字后值[" + digitOnly + "]");
        switch (headerType) {
            case OPEN:
                dayK.setOpen(new BigDecimal(digitOnly));
                break;
            case CLOSE:
                dayK.setClose(new BigDecimal(digitOnly));
                break;
            case HIGH:
                dayK.setHigh(new BigDecimal(digitOnly));
                break;
            case LOW:
                dayK.setLow(new BigDecimal(digitOnly));
                break;
            case UP_DOWN:
                dayK.setUpDown(new BigDecimal(digitOnly));
                break;
            case UP_DOWN_RATE:
                if (value.contains("%")) {
                    dayK.setUpDownRate(new BigDecimal(digitOnly).divide(new BigDecimal(100),
                        6, BigDecimal.ROUND_DOWN));
                } else {
                    dayK.setUpDownRate(new BigDecimal(digitOnly));
                }
                break;
            case AVERAGE:
                dayK.setAverage(new BigDecimal(digitOnly));
                break;
            case VOLUME:
                dayK.setVolume((int) Double.parseDouble(digitOnly));
                break;
            case TRUN_OVER:
                dayK.setTurnOver(new BigDecimal(digitOnly));
                break;
        }
    }

    /**
     * 列类型枚举
     */
    private enum ColumnTypeEnum {
        INST_ID,
        OPEN,
        CLOSE,
        HIGH,
        LOW,
        UP_DOWN,
        UP_DOWN_RATE,
        AVERAGE,
        VOLUME,
        TRUN_OVER
    }

    /**
     * 创建一个日K对象.
     *
     * @return 新的日K对象
     */
    private static DayK newDayK() {
        return new DayK();
    }

    /**
     * 日K对象
     */
    private static class DayK {

        private String url;
        /**
         * 所有黄金交易二级系统表都包含的合约代码字段.
         */
        private String instId;

        /**
         * 开盘价，对于分钟k来说就是分钟内第一笔交易价格，对于日k及以上就是开盘价，收盘价同理.
         */
        private BigDecimal open;

        /**
         * 最高价.
         */
        private BigDecimal high;

        /**
         * 最低价.
         */
        private BigDecimal low;

        /**
         * 收盘价.
         */
        private BigDecimal close;

        /**
         * 周期内成交量（双边），多少手交易，黄金和白银一手1kg，迷你黄金、黄金单月、黄金双月一手100g.
         */
        private Integer volume;

        /**
         * 涨跌价格，和上一周期的收盘价对比.
         */
        private BigDecimal upDown;

        /**
         * 涨跌幅度，和上一周期的收盘价对比.
         */
        private BigDecimal upDownRate;


        /**
         * 均价，单位：白银 - 元/kg，黄金 - 元/g.
         */
        private BigDecimal average;

        /**
         * 周期内总成交额.
         */
        private BigDecimal turnOver;

        /**
         * k线数据代表的日期：yyyyMMdd.
         */
        private Integer timeDay;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "DayK{" +
                "url='" + url + '\'' +
                ", instId='" + instId + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", upDown=" + upDown +
                ", upDownRate=" + upDownRate +
                ", average=" + average +
                ", turnOver=" + turnOver +
                ", timeDay=" + timeDay +
                '}';
        }

        public String getInstId() {
            return instId;
        }

        public void setInstId(String instId) {
            this.instId = instId;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public BigDecimal getClose() {
            return close;
        }

        public void setClose(BigDecimal close) {
            this.close = close;
        }

        public Integer getVolume() {
            return volume;
        }

        public void setVolume(Integer volume) {
            this.volume = volume;
        }

        public BigDecimal getUpDown() {
            return upDown;
        }

        public void setUpDown(BigDecimal upDown) {
            this.upDown = upDown;
        }

        public BigDecimal getUpDownRate() {
            return upDownRate;
        }

        public void setUpDownRate(BigDecimal upDownRate) {
            this.upDownRate = upDownRate;
        }

        public BigDecimal getAverage() {
            return average;
        }

        public void setAverage(BigDecimal average) {
            this.average = average;
        }

        public BigDecimal getTurnOver() {
            return turnOver;
        }

        public void setTurnOver(BigDecimal turnOver) {
            this.turnOver = turnOver;
        }

        public Integer getTimeDay() {
            return timeDay;
        }

        public void setTimeDay(Integer timeDay) {
            this.timeDay = timeDay;
        }
    }


    /**
     * 已知合约代码枚举.
     */
    private enum InstIdEnum {
        AG_DEFER(0, "Ag(T+D)", "白银延期"),
        AU_DEFER(1, "Au(T+D)", "黄金延期"),
        M_AU_DEFER(2, "mAu(T+D)", "迷你黄金延期"),
        AU_DEFER_N1(3, "Au(T+N1)", "黄金单月延期"),
        AU_DEFER_N2(4, "Au(T+N2)", "黄金双月延期"),

        AG_FORWARD1(5, "Ag99.9", "白银远期999"),
        AG_FORWARD2(6, "Ag99.99", "白银远期99.99"),

        AU_SPOT1(7, "Au99.5", "黄金现货995"),
        AU_SPOT2(8, "Au99.95", "黄金现货9995"),
        AU_SPOT3(9, "Au99.99", "黄金现货9999"),
        AU_SPOT4(10, "Au50g", "50g小金条"),
        AU_SPOT5(11, "Au100g", "100g金条"),
        IAU_SPOT1(12, "iAu99.5", "伦敦金现货995"),
        IAU_SPOT2(13, "iAu99.99", "伦敦金现货9999"),
        IU_SPOT3(14, "iAu100g", "伦敦100g金条"),
        PT_SPOT(15, "Pt99.95", "铂金现货9995");

        private final Integer order;
        private final String code;
        private final String desc;

        InstIdEnum(final Integer order, final String code, final String desc) {
            this.order = order;
            this.code = code;
            this.desc = desc;
        }

        private static InstIdEnum instance(final String code) {
            if (code == null) return null;
            for (InstIdEnum e : values()) {
                if (e.code.replace(".", "").equals(code)) return e;
            }
            return null;
        }
    }
}
