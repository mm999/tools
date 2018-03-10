package com.xiafei.tools;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.xiafei.tools.sftp.SftpProperties;
import com.xiafei.tools.utils.JSONUtil;
import com.xiafei.tools.utils.JvmCachePool;
import com.xiafei.tools.utils.JvmExCache;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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

    private static final JvmExCache<Integer> CACHE = new JvmExCache<>(10000L, true, false);
    private static int COUNT = 0;
    private static int COUNT_POOL = 0;
    //    @Resource
//    private Sftp sftp;
    @Resource
    private JvmCachePool cachePool;
    private static byte[] fileBytes;
    @Resource
    private SftpProperties sftpProperties;

//    @Value("${from}")
//    private String from;

    static {
        final ClassPathResource classPathResource = new ClassPathResource("IMG_0004.JPG");
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
//        String randomStr = UUID.randomUUID().toString();
//        sftp.uploadSync("/data/tempTest/" + randomStr + "/example.jpg", fileBytes);
//        sftp.getBytes("/data//tempTest/" + randomStr + "/example.jpg");
//        sftp.removeAsync("/data/tempTest/" + randomStr + "/example.jpg", false);
//        sftp.removeAsync("/data/tempTest/" + randomStr, true);
        return "complete";
    }


    @GetMapping("jvmCache")
    public Integer jvmCacheTest() throws Exception {
        return CACHE.getAndRefreshIfEx(() -> ++COUNT);
    }


    @GetMapping("jvmCachePool")
    public Integer jvmCachePoolTest() throws Exception {
        return cachePool.getAndRefreshIfExpire("testController", this, () -> ++COUNT_POOL);
    }

//    @GetMapping("config")
//    public String getConfig() {
////        return from;
//    }


    @GetMapping("sftPro")
    public String sftpProperties() {
        return JSONUtil.toJson(sftpProperties);
    }
}
