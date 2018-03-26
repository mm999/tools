package com.xiafei.tools.common;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

/**
 * <P>Description: 加载字体工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/22</P>
 * <P>UPDATE DATE: 2017/11/22</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class FontUtils {

    public static Font getFont() throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT,
                FontUtils.class.getResourceAsStream("simsun.ttc")).
                deriveFont(
                        // 字体粗细
                        Font.BOLD,
                        // 字体大小
                        10);
    }
}
