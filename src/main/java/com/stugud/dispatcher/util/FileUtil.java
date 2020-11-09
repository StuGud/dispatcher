package com.stugud.dispatcher.util;

import com.stugud.dispatcher.controller.EmployeeController;
import com.stugud.dispatcher.entity.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created By Gud on 2020/11/2 1:01 上午
 */
@Component
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Value("${dispatcher.commit.root}")
    private String root;

    /**
     * 根据commit的taskId，以及commitNo来储存文件
     *
     * @param commit
     * @return 生成的文件路径
     */
    public String storeCommit(Commit commit, MultipartFile file) {
        String filePath = root + "task" + commit.getTaskId() + "/" + "commit" + commit.getCommitNo() + "/" + file.getOriginalFilename();
        Path path = Paths.get(filePath);
        LOGGER.info("commit{}正在存入文件{}",commit,filePath);
        try {
            Files.createDirectories(path.getParent());
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return filePath;
    }

    public void downloadFile(HttpServletResponse response, String filePath) throws UnsupportedEncodingException {
        if (filePath==null){
            LOGGER.info("企图下载空文件");
            return;
        }
        File file=new File(filePath);
        String fileName=file.getName();
        // 配置文件下载
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));

        // 实现文件下载
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        }
        catch (Exception e) {
            LOGGER.info("文件{}下载失败",fileName);
        }
        finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
