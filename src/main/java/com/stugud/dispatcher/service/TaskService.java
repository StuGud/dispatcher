package com.stugud.dispatcher.service;

import com.stugud.dispatcher.entity.Task;

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
     * 发布任务
     * @param task
     * @return
     */
    Task release(Task task);

    /**
     * 修改任务
     * @param task
     * @return
     */
    Task modify(Task task);

    /**
     * 设置状态为已完成，并计算scoreChange
     * @return
     */
    Task setCompleted(long taskId);

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
    List<Task> findTaskPage(int pageNum);

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
    Task releaseByInChargeUsername(Task task);
}
