package com.irappelt.mymusic.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.irappelt.mymusic.aop.exception.FileAnalysisException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云 OSS存储接口
 *
 * @author: huaiyu
 * @date: Created in 2021/2/25 10:58
 */
public class OssStorage {

    /**
     * endPoint
     */
    private static final String END_POINT = "oss-cn-beijing.aliyuncs.com";

    /**
     * accessKeyId
     */
    private static final String ACCESS_KEY_ID = "LTAI4G5ngH5uGLwUEVD6gHiR";

    /**
     * accessKeySecret
     */
    private static final String ACCESS_KEY_SECRET = "ul1eW8xst5WDl1Rb05aPVessGVTGES";

    /**
     * songImageBucketName
     */
    private static final String SONG_IMAGE_BUCKET_NAME = "irappelt-song-image";

    /**
     * songFileBucketName
     */
    private static final String SONG_FILE_BUCKET_NAME = "irappelt-song-file";


    /**
     * 文件上传
     *
     * @param songName    歌曲名
     * @param imageFormat 歌曲图片后缀
     * @param songImage   歌曲图片源
     * @param songFile    歌曲文件源
     * @return
     */
    public static Map<String, String> multipartFileUpload(String songName, String imageFormat, MultipartFile songImage, MultipartFile songFile) {

        Map<String, String> result = new HashMap<>(16);
        OSS ossClient = null;
        // 生成文件名
        String songImageName = createFileName(songName) + "." + imageFormat;
        String songFileName = createFileName(songName) + ".mp3";
        try (InputStream imageInputStream = songImage.getInputStream();
             InputStream fileInputStream = songFile.getInputStream()) {
            // OSS
            ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            // 上传歌曲图片
            ossClient.putObject(SONG_IMAGE_BUCKET_NAME, songImageName, imageInputStream);
            // 上传歌曲文件
            ossClient.putObject(SONG_FILE_BUCKET_NAME, songFileName, fileInputStream);
            // 生成URL
            String songImageUrl = "https://" + SONG_IMAGE_BUCKET_NAME + "." + END_POINT + "/" + songImageName;
            String songFileUrl = "https://" + SONG_FILE_BUCKET_NAME + "." + END_POINT + "/" + songFileName;

            result.put("songImageUrl", songImageUrl);
            result.put("songFileUrl", songFileUrl);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileAnalysisException(e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static void multipartFileDownload(String url, HttpServletResponse response) {
        try {
            //url 为图片的url
            URL urls = new URL(url);
            // 打开连接
            URLConnection con = urls.openConnection();
            // 设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            // 输入流
            InputStream in = con.getInputStream();
            // 取出文件名称
            String[] strArray = url.split("/");
            String fileName = strArray[strArray.length - 1];

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            // 缓冲文件输出流
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            // 通知浏览器以附件形式下载
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 开始读取
            while ((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            out.close();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new FileAnalysisException(e.getMessage());
        }

    }

    /**
     * 生成文件名
     *
     * @param fileName 文件原始名
     * @return 当前时间格式yyMMddHHmmss+利用时间戳得到8位不重复的随机数+原始文件名的base64编码
     */
    private static String createFileName(String fileName) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String part1 = sdf.format(date);
        String part2 = Integer.toHexString((int) System.currentTimeMillis());
        String part3 = Base64.getEncoder().encodeToString(fileName.getBytes());
        return part1 + part2 + part3;
    }

    public static void main(String[] args) {
        System.out.println(Integer.toHexString((int) System.currentTimeMillis()));
    }
}