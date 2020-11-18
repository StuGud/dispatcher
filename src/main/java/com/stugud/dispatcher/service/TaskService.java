package com.stugud.dispatcher.service;

import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Task;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TaskService {
    /**
     * 根据任务号查找任务
     * @param id
     * @return
     */
    Task findById(long id);

    /**
     * 根据员工号查找他负责的任务
     * @param empId
     * @return
     */
    List<Task> findAllByEmpId(long empId);

    /**
     * 返回所有的任务
     * @return
     */
    List<Task> findAll();

    /**
     * 分页返回所有的任务
     * @param pageNum
     * @return
     */
    List<Task> findAllByPageNum(int pageNum);

    /**
     * 返回未完成的所有任务
     * @return
     */
    List<Task> findAllNotCompleted();

    /**
     * 分页返回未完成的所有任务
     * @param pageNum
     * @return
     */
    List<Task> findAllNotCompleted(int pageNum);

    /**
     * 使用负责人的姓名查找负责人信息并发布任务
     * @param task
     * @return
     */
    Task releaseWithInChargesName(Task task, MultipartFile file);

    List<Task> findAllByEmpIdAndState(long empId,String state);

    void downloadFile(HttpServletResponse response,long taskId);

    /**
     * 修改任务；不会修改任务状态，任务状态需要单独设置
     * Todo 修改任务文件，需要先删除原有文件，再upload
     * @param task
     * @return
     */
    Task modify(Task task);

    /**
     * 设置状态为已完成,使用当前时间为完成时间，并计算scoreChange
     * @return
     */
    Task setCompleted(long taskId);

    /**
     * 任务状态 已完成 -> 未完成 时使用，会回滚之前的得分
     * @param taskId
     * @return
     */
    Task setNotCompleted(long taskId) throws Exception;


}
