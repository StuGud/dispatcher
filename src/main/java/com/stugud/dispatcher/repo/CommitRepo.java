package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created By Gud on 2020/11/2 1:15 上午
 */
public interface CommitRepo extends JpaRepository<Commit,Long> {
    List<Commit> findAllByTaskId(Long taskId);
    List<Commit> findAllByEmployeeId(Long employeeId);
    List<Commit> findAllByState(int state);
}
