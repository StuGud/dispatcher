package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created By Gud on 2020/11/2 1:15 上午
 */
public interface CommitRepo extends JpaRepository<Commit,Long> {
    List<Commit> findAllByTaskId(Long taskId);
    List<Commit> findAllByEmployeeId(Long employeeId);
    List<Commit> findAllByState(int state);

    @Query(value = "select * from t_commit where commitNo= (select MAX(t_commit.commitNo) from  t_commit where taskId= ?1 ) and taskId= ?1 ;",nativeQuery = true)
    Commit findMaxCommitNoByTaskId(long taskId);
}
