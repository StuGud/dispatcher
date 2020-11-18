package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.service.CommitService;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created By Gud on 2020/11/15 2:06 下午
 */
@Controller
@RequestMapping("/admin")
public class AdminCommitController {

    @Autowired
    TaskService taskService;

    @Autowired
    CommitService commitService;

    /**
     * @param model
     * @param state
     * @return
     */
    @GetMapping("/commits")
    public String showCommitsPage(Model model, String state) {
        List<Commit> commits = null;
        if (state.equals("pending")) {
            commits = commitService.findAllByState(0);
        } else if (state.equals("notPassed")) {
            commits = commitService.findAllByState(1);
        } else if (state.equals("passed")) {
            commits = commitService.findAllByState(2);
        }
        model.addAttribute("commits", commits);
        return "admin/task/commit/commits";
    }

    /**
     * Todo commit设置为完成时应该在commitService中保存task？
     * @param commitId
     * @param reply
     * @return
     */
    @GetMapping("/commit/{commitId}/setPassed")
    @ResponseBody
    public Commit setCommitPassed(@PathVariable(name = "commitId") long commitId, String reply) {
        Commit passedCommit = commitService.reply(commitId,2, reply);
        Task task = taskService.setCompleted(passedCommit.getTaskId());
        return passedCommit;
    }

    @GetMapping("/commit/{commitId}/setNotPassed")
    @ResponseBody
    public Commit setCommitNotPassed(@PathVariable(name = "commitId") long commitId, String reply) {
        Commit passedCommit = commitService.reply(commitId,1, reply);
        return passedCommit;
    }

    @GetMapping("/commit/{commitId}/download")
    public void downloadCommit(@PathVariable(name = "commitId") long commitId,
                               HttpServletResponse response) {
        Commit commit = commitService.findById(commitId);
        commitService.downloadFile(response, commit);
    }
}
