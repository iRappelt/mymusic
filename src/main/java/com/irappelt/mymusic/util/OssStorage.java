package com.irappelt.mymusic.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.irappelt.mymusic.aop.exception.FileAnalysisException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: huaiyu
 * @date: Created in 2021/2/25 10:58
 */
public class OssStorage {

    private static final String END_POINT = "oss-cn-beijing.aliyuncs.com";

    private static final String ACCESS_KEY_ID = "LTAI4G5ngH5uGLwUEVD6gHiR";

    private static final String ACCESS_KEY_SECRET = "ul1eW8xst5WDl1Rb05aPVessGVTGES";

    private static final String SONG_IMAGE_BUCKET_NAME = "irappelt-song-image";

    private static final String SONG_FILE_BUCKET_NAME = "irappelt-song-file";


    public static Map<String, String> multipartFileUpload(String imageFormat, MultipartFile songImage, MultipartFile songFile) {

        Map<String, String> result = new HashMap<>();
        OSS ossClient = null;
        // 生成文件名
        String songImageName = createFileName()+"."+imageFormat;
        String songFileName = createFileName()+".mp3";
        try (InputStream imageInputStream = songImage.getInputStream();
             InputStream fileInputStream = songFile.getInputStream()) {
            // OSS
            ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            // 上传歌曲图片
            ossClient.putObject(SONG_IMAGE_BUCKET_NAME, songImageName, imageInputStream);
            // 上传歌曲文件
            ossClient.putObject(SONG_FILE_BUCKET_NAME, songFileName, fileInputStream);
            // 生成URL
            String songImageUrl = "https://"+SONG_IMAGE_BUCKET_NAME+"."+ END_POINT +"/"+songImageName;
            String songFileUrl = "https://"+SONG_FILE_BUCKET_NAME+"."+ END_POINT +"/"+songFileName;

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

    private static String createFileName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String part1 = sdf.format(date);
        String part2 = Integer.toHexString((int) date.getTime());

        return part1 + part2;
    }
}
