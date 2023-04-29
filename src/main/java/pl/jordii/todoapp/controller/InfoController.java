package pl.jordii.todoapp.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jordii.todoapp.config.TaskConfigurationProperties;

@RestController
@RequestMapping("/info")
public class InfoController {

    private TaskConfigurationProperties config;

    public InfoController(@Qualifier("taskConfigurationProperties") TaskConfigurationProperties config) {
        this.config = config;
    }

    @GetMapping(value = "/url")
    public boolean multipletask() {
        return config.getTemplate().isAllowMultipleTasks();
    }
}
