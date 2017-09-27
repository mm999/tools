package com.xiafei.tools.spider.metalquotation;

import java.math.BigDecimal;

/**
 * <P>Description:  . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/23</P>
 * <P>UPDATE DATE: 2017/8/23</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class DayK {

    /**
     * 分析用，和最终结果无关.
     */
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


    private Integer weekYear;

    private Integer index;

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
                ", weekYear=" + weekYear +
                ", index=" + index +
                '}';
    }


    public Integer getWeekYear() {
        return weekYear;
    }

    public void setWeekYear(final Integer weekYear) {
        this.weekYear = weekYear;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(final Integer index) {
        this.index = index;
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
