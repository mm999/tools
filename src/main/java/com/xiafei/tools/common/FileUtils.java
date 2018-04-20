package com.xiafei.tools.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.PathResource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
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
    public static void outPutToFileByLine(final String filePath, final List<?> content, final boolean isNewFile) {
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
            outPutToFileByLine(file, content, isNewFile);

        } else {
            throw new RuntimeException("文件路径必须包含文件名和文件类型" + filePath);
        }
    }

    /**
     * 按行输出内容到文件
     *
     * @param content   要输出的内容列表，列表每一个字符串代表文件一行
     * @param isNewFile 是否替换原文件，若为否，则在源文件上追加（若存在）
     */
    public static void outPutToFileByLine(final File file, final List<?> content, final boolean isNewFile) {
        try (FileOutputStream outputStream = new FileOutputStream(file, true);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {

            file.createNewFile();
            if (!isNewFile) {
                bufferedWriter.write("#########################################################");
                bufferedWriter.newLine();
                bufferedWriter.write("################" + DateUtils.getYMDHMSWithSeparate().format(new Date()) + "追加#################");
                bufferedWriter.newLine();
                bufferedWriter.write("#########################################################");
                bufferedWriter.newLine();
            }
            for (Object line : content) {
                bufferedWriter.write(line == null ? "" : line.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("创建文件失败，请检查文件夹是否已经建好" + file.getAbsolutePath());

        }

    }

    /**
     * 将字符串输出到文件.
     *
     * @param filePath
     * @param content
     */
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

    /**
     * 获取类相对路径的文件输入流.
     *
     * @param cls  路径起始类
     * @param path 基于起始类的相对路径
     * @return 文件的输入流
     */
    public static InputStream getStream(final Class cls, final String path) {
        return cls.getResourceAsStream(path);
    }


    public static String readFileToBase64(final String path) {
        PathResource pathResource = new PathResource(path);
        try (InputStream is = pathResource.getInputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8096];
            int remain = 0;
            while ((remain = is.read(buffer, 0, buffer.length)) > 0) {
                bos.write(buffer, 0, remain);
            }

            byte[] content = bos.toByteArray();
            return Base64.encodeBase64String(content);
        } catch (IOException e) {
            log.error("readFileToBase64(),关闭流失败", e);
            return null;
        }

    }

    public static List<String> readFileToString(final File file) {
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            final List<String> result = new ArrayList<>();
            String temp;
            while ((temp = br.readLine()) != null) {
                result.add(temp);
            }
            return result;
        } catch (FileNotFoundException e) {
            log.error("readFileToString(),找不到文件错误", e);
        } catch (IOException e) {
            log.error("readFileToString,读取文件错误");
        }
        return null;
    }
}
