package com.stugud.dispatcher.service;

import com.stugud.dispatcher.entity.Commit;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created By Gud on 2020/11/2 12:53 上午
 */
@Service
public interface CommitService {
    Commit findById(long commitId);
    List<Commit> findAllByEmpId(long empId);
    List<Commit> findAllByTaskId(long taskId);
    List<Commit> findAllByState(int state);
    Commit findLastPassedCommitByTaskId(long taskId);
    Commit commit(Commit commit,MultipartFile file);
    void downloadFile(HttpServletResponse response,Commit commit);

    /**
     * 通过commit
     * 需要设置reply、state
     * @return
     */
    Commit setPassed(long commitId, String reply);
}
