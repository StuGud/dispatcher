package com.stugud.dispatcher.util;

import com.stugud.dispatcher.entity.Commit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created By Gud on 2020/11/2 1:01 上午
 */
@Component
public class FileUtil {

    @Value("${dispatcher.commit.root}")
    private String root;

    /**
     * 根据commit的taskId，以及commitNo来储存文件
     * @param commit
     * @return 生成的文件路径
     */
    public String storeCommit(Commit commit, MultipartFile file){
        String filePath="";
        return filePath;
    }
}
