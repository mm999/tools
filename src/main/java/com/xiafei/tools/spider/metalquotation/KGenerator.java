package com.xiafei.tools.spider.metalquotation;

import com.xiafei.tools.common.BeanUtils;
import com.xiafei.tools.common.DateUtils;
import com.xiafei.tools.common.FileUtils;
import com.xiafei.tools.common.MapUtils;
import com.xiafei.tools.common.StringUtils;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * <P>Description:  . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/22</P>
 * <P>UPDATE DATE: 2017/8/22</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class KGenerator {

    /**
     * 一个空格.
     */
    private static final String BLANK = "";

    /**
     * 逗号.
     */
    private static final String COMMA = ",";

    public static void main(String[] args) throws IntrospectionException, ReflectiveOperationException {
        final List<String> dataList = getKDataList();
        final List<DayK> dayKList = convertStrKToObjK(dataList);
        // 生成日K
//        generalDayK(dayKList);
        // 生成周K和月K
        generalWeekMonthK(dayKList);
    }

    /**
     * K线页面拔下来的数据转换成对象.
     */
    private static List<DayK> convertStrKToObjK(final List<String> dataList) throws IntrospectionException, ReflectiveOperationException {
        final List<DayK> dayKList = new ArrayList<>(dataList.size());
        for (String k : dataList) {
            final DayK dayK = BeanUtils.parseKeyValueStrToBean(k.replace("'", "").replace("}", ""), ",", false, DayK.class, null);
            dayKList.add(dayK);
        }
        return dayKList;
    }

    /**
     * 生成日K.
     *
     * @param dataList 数据列表
     */
    private static void generalDayK(final List<DayK> dataList) {
        final List<String> kSqlList = new ArrayList<>();
        for (DayK k : dataList) {
            try {
                kSqlList.add(getOneKSql(k, KLineTypeEnum.DAY));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("异常" + k);
                return;
            }
        }

        FileUtils.outPutToFileByLine("E:\\self-study\\tools\\src\\mail\\java\\com\\xiafei\\tools\\spider\\metalquotation\\dayKInsertSql.sql", kSqlList, true);

    }

    /**
     * 生成周K月K.
     *
     * @param dataList
     */
    private static void generalWeekMonthK(final List<DayK> dataList) throws ReflectiveOperationException, IntrospectionException {

        final Map<String, List<DayK>> groupByInstMap = MapUtils.newHashMap();
        for (DayK k : dataList) {
            if (groupByInstMap.get(k.getInstId()) == null) {
                groupByInstMap.put(k.getInstId(), new ArrayList<DayK>());
            }
            groupByInstMap.get(k.getInstId()).add(k);
        }
        generalWeek(groupByInstMap);
        generalMonth(groupByInstMap);

    }

    /**
     * 生成周k.
     *
     * @param groupByInstMap 按照合约编码分组的日K信息列表
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void generalWeek(final Map<String, List<DayK>> groupByInstMap) throws ReflectiveOperationException, IntrospectionException {
        final List<DayK> weekKList = new ArrayList<>();

        // 生成周K
        // 确定交易日期是一年中的第几周
        // 这里将一周的开始日期设定为周六，则一周的结束日期就是周五，符合业务逻辑
        for (Map.Entry<String, List<DayK>> entry : groupByInstMap.entrySet()) {
            List<DayK> list = entry.getValue();
            for (int i = 0, len = list.size(); i < len; ) {
                final DayK dayK = list.get(i);
                final DayK weekK = new DayK();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.parse(dayK.getTimeDay(), DateUtils.getYMD()));
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);
                org.apache.commons.beanutils.BeanUtils.copyProperties(weekK, dayK);
                weekK.setIndex(calendar.get(Calendar.WEEK_OF_YEAR));
                weekK.setWeekYear(Integer.parseInt(String.valueOf(weekK.getTimeDay()).substring(0, 4)));
                if (weekK.getIndex() == 1) {
                    final int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                    if (dayOfYear > 7) {
                        // 若是一年的年初日期肯定是小于等于7的，否则就是一年的年底
                        weekK.setWeekYear(weekK.getWeekYear() + 1);
                    }
                }

                weekKList.add(weekK);
                i++;

                // 内循环处理完本周内的数据
                for (int j = i; j < len; j++) {
                    final DayK innoDayK = list.get(j);
                    final Calendar calendarInno = Calendar.getInstance();
                    calendarInno.setTime(DateUtils.parse(innoDayK.getTimeDay(), DateUtils.getYMD()));
                    calendarInno.setFirstDayOfWeek(Calendar.SATURDAY);
                    final Integer index = calendarInno.get(Calendar.WEEK_OF_YEAR);
                    final Integer weekYear = Integer.valueOf(String.valueOf(innoDayK.getTimeDay()).substring(0, 4));
                    if (!index.equals(weekK.getIndex())
                            || (index != 1 && !weekYear.equals(weekK.getWeekYear()))
                            || (index == 1 && weekYear - weekK.getWeekYear() > 1)) {
                        break;
                    }
                    i++;
                    setWeekMonthProperties(innoDayK, weekKList);
                }
            }
        }

        final List<String> weekSqlList = new ArrayList<>(weekKList.size());
        for (DayK weekK : weekKList) {
            weekSqlList.add(getOneKSql(weekK, KLineTypeEnum.WEEK));
        }
        FileUtils.outPutToFileByLine("E:\\self-study\\tools\\src\\mail\\java\\com\\xiafei\\tools\\spider\\metalquotation\\weekKInsertSql.sql", weekSqlList, true);

    }

    /**
     * 生成月K.
     *
     * @param groupByInstMap
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void generalMonth(final Map<String, List<DayK>> groupByInstMap) throws ReflectiveOperationException, IntrospectionException {
        final List<DayK> monthKList = new ArrayList<>();

        // 生成月K
        for (Map.Entry<String, List<DayK>> entry : groupByInstMap.entrySet()) {
            List<DayK> list = entry.getValue();
            for (int i = 0, len = list.size(); i < len; ) {
                final DayK dayK = list.get(i);
                final DayK monthK = new DayK();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.parse(dayK.getTimeDay(), DateUtils.getYMD()));
                org.apache.commons.beanutils.BeanUtils.copyProperties(monthK, dayK);
                monthK.setIndex(calendar.get(Calendar.MONTH));
                monthK.setWeekYear(Integer.parseInt(String.valueOf(monthK.getTimeDay()).substring(0, 4)));

                monthKList.add(monthK);
                i++;
                // 内循环处理完本月内的数据
                for (int j = i; j < len; j++) {
                    final DayK innoDayK = list.get(j);
                    final Calendar calendarInno = Calendar.getInstance();
                    calendarInno.setTime(DateUtils.parse(innoDayK.getTimeDay(), DateUtils.getYMD()));
                    final Integer index = calendarInno.get(Calendar.MONTH);
                    final Integer weekYear = Integer.valueOf(String.valueOf(innoDayK.getTimeDay()).substring(0, 4));
                    if (!index.equals(monthK.getIndex()) || !weekYear.equals(monthK.getWeekYear())) {
                        break;
                    }
                    i++;
                    setWeekMonthProperties(innoDayK, monthKList);
                }
            }

        }

        final List<String> monthKSqlList = new ArrayList<>(monthKList.size());
        for (DayK monthK : monthKList) {
            monthKSqlList.add(getOneKSql(monthK, KLineTypeEnum.MONTH));
        }

        FileUtils.outPutToFileByLine("E:\\self-study\\tools\\src\\mail\\java\\com\\xiafei\\tools\\spider\\metalquotation\\monthKInsertSql.sql", monthKSqlList, true);
    }

    /**
     * 设置周K或月K的属性.
     *
     * @param innoDayK 内循环到的日K数据对象
     * @param kList    周K或者月K列表
     */
    private static void setWeekMonthProperties(final DayK innoDayK, final List<DayK> kList) {
        DayK k = kList.get(kList.size() - 1);
        k.setTimeDay(innoDayK.getTimeDay());
        if (innoDayK.getHigh() != null
                && (k.getHigh() == null || k.getHigh().compareTo(innoDayK.getHigh()) < 0)) {
            k.setHigh(innoDayK.getHigh());
        }

        if (innoDayK.getLow() != null
                && (k.getLow() == null || k.getLow().compareTo(innoDayK.getLow()) > 0)) {
            k.setLow(innoDayK.getLow());
        }

        if (innoDayK.getVolume() != null) {
            k.setVolume(k.getVolume() == null ? 0 : k.getVolume() + innoDayK.getVolume());
        }

        if (innoDayK.getClose() != null) {
            k.setClose(innoDayK.getClose());
        }

        if (innoDayK.getTurnOver() != null) {
            k.setTurnOver((k.getTurnOver() == null ? BigDecimal.ZERO : k.getTurnOver())
                    .add(innoDayK.getTurnOver()));
        }

        if (kList.size() > 1) {
            final DayK lastK = kList.get(kList.size() - 2);
            if (k.getClose() == null) {
                if (lastK.getClose() == null) {
                    k.setUpDown(BigDecimal.ZERO);
                    k.setUpDownRate(BigDecimal.ZERO);
                } else {
                    k.setUpDown(lastK.getClose().multiply(new BigDecimal("-1")));
                    k.setUpDownRate(BigDecimal.ONE.multiply(new BigDecimal("-1")));
                }
            } else {
                if (lastK.getClose() == null) {
                    k.setUpDown(k.getClose());
                    k.setUpDownRate(BigDecimal.ONE);

                } else {
                    k.setUpDown(k.getClose().subtract(lastK.getClose()));
                    k.setUpDownRate(k.getUpDown().divide(lastK.getClose(), 6, BigDecimal.ROUND_DOWN));
                }
            }
        } else {
            k.setUpDown(k.getClose());
            k.setUpDownRate(BigDecimal.ONE);
        }


    }

    /**
     * 构建一个K的插入sql.
     *
     * @param k K线数据对象
     * @return 插入sql语句
     */
    private static String getOneKSql(final DayK k, final KLineTypeEnum type) throws IntrospectionException, ReflectiveOperationException {
        final StringBuilder sb = new StringBuilder("INSERT INTO metal_gold_k_");

        final DayKSpider.InstIdEnum suffixEnum = DayKSpider.InstIdEnum.instance(k.getInstId());
        if (suffixEnum == null) {
            throw new IllegalArgumentException("找不到合约代码和表名对应关系" + k);
        }
        sb.append(suffixEnum.suffix).append(BLANK);
        sb.append("(`inst_id`,`type`,`open`,`high`,`low`,`close`,`volume`,`up_down`,`up_down_rate`,`average`,`turn_over`" +
                ",`sequence_no`,`time_day`,`time_minute`,`week_year`,`index`,`create_time`)");
        sb.append("VALUES(");
        sb.append("'").append(suffixEnum.code).append("'").append(COMMA);
        sb.append(type.code).append(COMMA);
        sb.append(k.getOpen()).append(COMMA);
        sb.append(k.getHigh()).append(COMMA);
        sb.append(k.getLow()).append(COMMA);
        sb.append(k.getClose()).append(COMMA);
        sb.append(k.getVolume()).append(COMMA);
        sb.append(k.getUpDown()).append(COMMA);
        sb.append(k.getUpDownRate()).append(COMMA);
        sb.append(k.getAverage()).append(COMMA);
        sb.append(k.getTurnOver()).append(COMMA);
        sb.append("0").append(COMMA);
        sb.append(k.getTimeDay()).append(COMMA);
        sb.append("'0000'").append(COMMA);
        sb.append(k.getWeekYear()).append(COMMA);
        sb.append(k.getIndex()).append(COMMA);
        sb.append("sysdate()");
        sb.append(");");
        return sb.toString();
    }


    /**
     * 从文件中读取K线数据.
     *
     * @return 每一个K线数据是一行数据的列表
     */
    private static List<String> getKDataList() {
        final List<String> result = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(
                        new FileReader("E:\\self-study\\tools\\src\\mail\\java\\com\\xiafei\\tools\\spider\\metalquotation\\dayk.txt"));
        ) {
            String line = reader.readLine();
            while (line != null) {
                String instId = StringUtils.getPropertyValueFromSimilarURL(line, "instId", ",").replace("'", "");
                if (DayKSpider.InstIdEnum.instance(instId) == null) {
                    System.out.println("异常的instid=" + instId + ",line=" + line);
                }
                result.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
