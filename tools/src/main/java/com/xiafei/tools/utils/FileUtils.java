package com.xiafei.tools.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

    public static void outPutToFileByLine(String filePath, List<String> content) {
        if (filePath == null || content == null || content.isEmpty()) {
            return;
        }
        // 校验是否包含文件名
        final String regex = "^.*\\.[a-z]+$";
        if (filePath.matches(regex)) {
            final File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            try (FileOutputStream outputStream = new FileOutputStream(file, true);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
                 BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
            ) {

                file.createNewFile();
                for (String line : content) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException("创建文件失败，请检查文件夹是否已经建好");

            }

        } else {
            throw new RuntimeException("文件路径必须包含文件名和文件类型");
        }
    }
}
