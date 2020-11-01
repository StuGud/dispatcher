package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.repo.CommitRepo;
import com.stugud.dispatcher.service.CommitService;
import com.stugud.dispatcher.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created By Gud on 2020/11/2 1:14 上午
 */
@Service
public class CommitServiceImpl implements CommitService {

    @Autowired
    CommitRepo commitRepo;

    @Autowired
    FileUtil fileUtil;

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
        return null;
    }

    @Override
    public Commit commit(Commit commit, MultipartFile file) {
        Commit savingCommit=new Commit(commit.getTaskId(),commit.getEmployeeId(),commit.getMessage());
        //得出最后一次commit的序号+1
        int commitNo=0;


        String filePath = fileUtil.storeCommit(savingCommit, file);
        savingCommit.setFilePath(filePath);
        savingCommit.setCommitNo(commitNo);

        return null;
    }
}
