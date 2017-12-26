package com.xiafei.tools;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.xiafei.tools.sftp.Sftp;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/26</P>
 * <P>UPDATE DATE: 2017/12/26</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private Sftp sftp;

    private static byte[] fileBytes;

    static {
        final ClassPathResource classPathResource = new ClassPathResource("Chrysanthemum.jpg");
        try (InputStream fis = classPathResource.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024 << 3];
            int remain = 0;
            while ((remain = fis.read(buffer)) > 0) {
                baos.write(buffer, 0, remain);
            }
            fileBytes = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/sftp")
    public String sftpTest() throws JSchException, SftpException, IOException {
        String randomStr = UUID.randomUUID().toString();
        sftp.uploadSync("/data/tempTest/" + randomStr + "/example.jpg", fileBytes);
        sftp.getBytes("/data//tempTest/" + randomStr + "/example.jpg");
        sftp.removeAsync("/data/tempTest/" + randomStr + "/example.jpg", false);
        sftp.removeAsync("/data/tempTest/" + randomStr, true);
        return "complete";
    }

}
