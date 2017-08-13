package com.xiafei.tools.utils;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * <P>Description:  文件操作工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/14</P>
 * <P>UPDATE DATE: 2017/7/14</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public final class FileUtils {
    private FileUtils() {

    }

    /**
     * 按行输出内容到文件
     *
     * @param filePath  文件完整路径（包含文件名及后缀）
     * @param content   要输出的内容列表，列表每一个字符串代表文件一行
     * @param isNewFile 是否替换原文件，若为否，则在源文件上追加（若存在）
     */
    public static void outPutToFileByLine(final String filePath, final List<String> content, final boolean isNewFile) {
        if (filePath == null || content == null || content.isEmpty()) {
            return;
        }
        // 校验是否包含文件名
        final String regex = "^.*\\.[a-z]+$";
        if (filePath.matches(regex)) {
            final File file = new File(filePath);
            boolean flag = file.exists();
            if (isNewFile && flag) {
                file.delete();
            }
            try (FileOutputStream outputStream = new FileOutputStream(file, true);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
                 BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
            ) {

                file.createNewFile();
                if (flag && !isNewFile) {
                    bufferedWriter.write("#########################################################");
                    bufferedWriter.newLine();
                    bufferedWriter.write("################" + DateUtils.getYMDHMSWithSeparate().format(new Date()) + "追加#################");
                    bufferedWriter.newLine();
                    bufferedWriter.write("#########################################################");
                    bufferedWriter.newLine();
                }
                for (String line : content) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException("创建文件失败，请检查文件夹是否已经建好" + filePath);

            }

        } else {
            throw new RuntimeException("文件路径必须包含文件名和文件类型" + filePath);
        }
    }

    public static void out(final String filePath, final String content) {
        if (filePath == null || content == null || content.isEmpty()) {
            return;
        }
        // 校验是否包含文件名
        final String regex = "^.*\\.[a-z]+$";
        if (filePath.matches(regex)) {
            final File file = new File(filePath);
            try (FileOutputStream outputStream = new FileOutputStream(file, true);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
                 BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
            ) {

                file.createNewFile();
                bufferedWriter.write(content);
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException("创建文件失败，请检查文件夹是否已经建好" + filePath);

            }

        } else {
            throw new RuntimeException("文件路径必须包含文件名和文件类型" + filePath);
        }
    }
}
