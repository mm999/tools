package com.xiafei.tools.spider.metalquotation;

import com.xiafei.tools.utils.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * <P>Description:  日K数据文件校验. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/22</P>
 * <P>UPDATE DATE: 2017/8/22</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public class DayKTest {

    public static void main(String[] args) {
        try (
                BufferedReader reader = new BufferedReader(
                        new FileReader("E:\\self-study\\tools\\src\\main\\java\\com\\xiafei\\tools\\spider\\metalquotation\\dayk.txt"));
        ) {
            String line = reader.readLine();
            String lastTimeDay = "0";
            while (line != null) {
                String instId = StringUtils.getPropertyValueFromSimilarURL(line, "instId", ",").replace("'", "");
                if (DayKSpider.InstIdEnum.instance(instId) == null) {
                    System.out.println("异常的instid=" + instId + ",line=" + line);
                }
                String timeDay = StringUtils.getPropertyValueFromSimilarURL(line, "timeDay", ",").replace("'", "").replace("}","");
                if (Integer.parseInt(lastTimeDay) > Integer.parseInt(timeDay)) {
                    System.out.println("时间顺序错误，报文:" + line);
                }
                lastTimeDay = timeDay;

                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
