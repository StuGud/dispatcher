package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.controller.EmployeeController;
import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.CommitRepo;
import com.stugud.dispatcher.repo.TaskRepo;
import com.stugud.dispatcher.service.CommitService;
import com.stugud.dispatcher.util.FileUtil;
import com.stugud.dispatcher.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

/**
 * Created By Gud on 2020/11/2 1:14 上午
 */
@Service
public class CommitServiceImpl implements CommitService {

    private static final Logger LOGGER= LoggerFactory.getLogger(EmployeeController.class);

    final
    CommitRepo commitRepo;

    final
    FileUtil fileUtil;

    @Autowired
    MailUtil mailUtil;

    @Autowired
    TaskRepo taskRepo;

    public CommitServiceImpl(CommitRepo commitRepo, FileUtil fileUtil) {
        this.commitRepo = commitRepo;
        this.fileUtil = fileUtil;
    }

    @Override
    public Commit findById(long commitId) {
        Optional<Commit> optionalCommit = commitRepo.findById(commitId);
        if (optionalCommit.isPresent()){
            return optionalCommit.get();
        }
        return null;
    }

    @Override
    public List<Commit> findAllByEmpId(long empId) {
        List<Commit> commits = commitRepo.findAllByEmployeeId(empId);
        return commits;
    }

    @Override
    public List<Commit> findAllByTaskId(long taskId) {
        List<Commit> tasks = commitRepo.findAllByTaskId(taskId);
        return tasks;
    }

    @Override
    public List<Commit> findAllByState(int state) {
        List<Commit> commits = commitRepo.findAllByState(state);
        return commits;
    }

    @Override
    public Commit findLastPassedCommitByTaskId(long taskId) {
        Commit commit = commitRepo.findMaxCommitNoByTaskId(taskId);
        return commit;
    }

    @Override
    public Commit commit(Commit commit, MultipartFile file) {
        Commit savingCommit=new Commit(commit.getTaskId(),commit.getEmployeeId(),commit.getMessage());
        savingCommit.setState(0);

        //得出最后一次commit的序号+1
        int commitNo=1;
        Commit MaxNoCommit = commitRepo.findMaxCommitNoByTaskId(commit.getTaskId());
        if (MaxNoCommit!=null){
            commitNo= MaxNoCommit.getCommitNo()+1;
        }
        savingCommit.setCommitNo(commitNo);

        String filePath = fileUtil.storeCommit(savingCommit, file);
        savingCommit.setFilePath(filePath);

        Commit savedCommit = commitRepo.save(savingCommit);
        return savedCommit;
    }

    @Override
    public void downloadFile(HttpServletResponse response, Commit commit) {
        if(null!=commit){
            try {
                fileUtil.downloadFile(response, commit.getFilePath());
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("下载文件失败{}",e);
            }
        }
    }

    @Override
    public Commit reply(long commitId,int state, String reply) {
        Optional<Commit> optionalCommit = commitRepo.findById(commitId);
        if(optionalCommit.isPresent()){
            Commit commit = optionalCommit.get();
            commit.setState(state);
            commit.setReply(reply);
            Commit savedCommit = commitRepo.save(commit);
            //没通过，发送邮件
            if(state==1){
                Optional<Task> optionalTask = taskRepo.findById(commit.getTaskId());
                if (optionalTask.isPresent()){
                    Task task=optionalTask.get();
                    mailUtil.sendCommitNotPassedMail("提交未通过! ",task,commit);
                }
            }
            return savedCommit;
        }
        return null;
    }
}
