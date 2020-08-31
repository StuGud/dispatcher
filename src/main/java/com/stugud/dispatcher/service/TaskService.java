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
     * 根据员工号查找他负责任务
     * @param empId
     * @return
     */
    List<Task> findByEmpId(long empId);

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
     * 设置状态为已完成，计算scoreChange
     * @return
     */
    Task setCompleted(Task task);

    /**
     * 返回所有的任务
     * @return
     */
    List<Task> getList();

    /**
     * 返回未完成的所有任务
     * @return
     */
    List<Task> getNotCompletedList();

}
