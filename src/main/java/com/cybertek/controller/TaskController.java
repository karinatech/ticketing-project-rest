package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.enums.Status;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name = "Task Controller",description = "Task API")
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage ="Something went wrong please try again ")
    @Operation(summary = "Read all tasks")
    @PreAuthorize("hasAuthority('Manager')")

    public ResponseEntity<ResponseWrapper>readAll(){
        return ResponseEntity.ok(new ResponseWrapper("Successfully read all tasks"));
    }
    @GetMapping("/{project-manager}")
    @DefaultExceptionMessage(defaultMessage ="Something went wrong please try again ")
    @Operation(summary = "Read all tasks by project manager")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper>readAllByProjectManager() throws TicketingProjectException {
        List<TaskDTO>listTasks=taskService.listAllTasksByProjectManager();
        return ResponseEntity.ok(new ResponseWrapper("Successfully retreived tasks by project manager",listTasks));

    }
    @GetMapping("/{id}")
    @DefaultExceptionMessage(defaultMessage ="Something went wrong please try again ")
    @Operation(summary = "Read tasks by id")
    @PreAuthorize("hasAnyAuthority('Manager','Employr')")
    public ResponseEntity<ResponseWrapper>readById(@PathVariable("id") Long id) throws TicketingProjectException {
        TaskDTO currentTask=taskService.findById(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retreive task ",currentTask));

    }
    @PostMapping
    @DefaultExceptionMessage(defaultMessage ="Something went wrong please try again ")
    @Operation(summary = "Create new task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper>create(@RequestBody TaskDTO taskDTO){
        TaskDTO createdTask = taskService.save(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully task created ",createdTask));

    }
    @DeleteMapping("/{id}")
    @DefaultExceptionMessage(defaultMessage ="Something went wrong please try again ")
    @Operation(summary = "Delete task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper>deelete(@PathVariable("id") Long id) throws TicketingProjectException {
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully deleted"));
    }
    @PutMapping
    @DefaultExceptionMessage(defaultMessage ="Something went wrong please try again ")
    @Operation(summary = "Update task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper>updateTask(@RequestBody TaskDTO taskDTO) throws TicketingProjectException {
        TaskDTO updateTask= taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Succcessfully updated taask"));
    }
    @GetMapping("/employee")
    @DefaultExceptionMessage(defaultMessage ="Something went wrong please try again ")
    @Operation(summary = "Read all non completed task")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<ResponseWrapper>employeeReadAllNonCompleteTask() throws TicketingProjectException {
    List<TaskDTO>list= taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
    return ResponseEntity.ok(new ResponseWrapper("Succeessfully retrieved not completed tasks",list));
    }
    @PutMapping("/employee-update")
    @DefaultExceptionMessage(defaultMessage ="Something went wrong please try again ")
    @Operation(summary = "Read all non completed task")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<ResponseWrapper>employeeUpdateTask(@RequestBody TaskDTO taskDTO) throws TicketingProjectException {
TaskDTO task = taskService.updateStatus(taskDTO);
return ResponseEntity.ok(new ResponseWrapper("Succesfullyu employe status updated ",task));
    }

}
