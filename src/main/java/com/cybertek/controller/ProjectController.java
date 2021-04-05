package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.enums.Status;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.service.ProjectService;
import com.cybertek.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/project")
@Tag(name = "User Controller",description = "User API")
public class ProjectController {

    private ProjectService projectService;
    private UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

@GetMapping
@Operation(summary = "Read all projects")
@DefaultExceptionMessage(defaultMessage = "Something went wrong")
@PreAuthorize("hasAnyAuthority('Admin', 'Manager')")

  public ResponseEntity<ResponseWrapper>readAll(){
        List<ProjectDTO>projects=projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("Projects are retrieved",projects));

  }
  @GetMapping("/{projectCode}")
  @Operation(summary = "Read project by project code")
  @DefaultExceptionMessage(defaultMessage = "Something went wrong")
  @PreAuthorize("hasAnyAuthority('Admin', 'Manager')")
    public ResponseEntity<ResponseWrapper>readByProjectCode(@PathVariable("projectCode") String projectCod){
       ProjectDTO project=projectService.getByProjectCode(projectCod);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved",project));
  }
    @PostMapping
    @Operation(summary = "Create project")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @PreAuthorize("hasAnyAuthority('Admin', 'Manager')")
    public ResponseEntity<ResponseWrapper>create(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {
       ProjectDTO project=projectService.save(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved",project));
    }
    @PutMapping
    @Operation(summary = "CUpdate project")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong")
    @PreAuthorize("hasAnyAuthority('Admin', 'Manager')")
    public ResponseEntity<ResponseWrapper>update(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {
        ProjectDTO project=projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved",project));
    }
    @DeleteMapping("/{projectcode}")
    @Operation(summary = "Delete project")
    @DefaultExceptionMessage(defaultMessage = "Fail to delete the project")
    @PreAuthorize("hasAnyAuthority('Admin', 'Manager')")
    public ResponseEntity<ResponseWrapper>delete(@PathVariable("projectCode") String projectCode) throws TicketingProjectException {
   projectService.delete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project is deleted"));
    }

    @PutMapping("/complete/{projectcode}")
    @Operation(summary = "Complete project")
    @DefaultExceptionMessage(defaultMessage = "Fail to complete the project")
    @PreAuthorize("hasAnyAuthority('Manager')")
    public ResponseEntity<ResponseWrapper>complete(@PathVariable("projectCode") String projectCode) throws TicketingProjectException {

       ProjectDTO projectDTO= projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project is completed",projectDTO));
    }

    @GetMapping("/deetails")
    @Operation(summary = "Read all project detaails")
    @DefaultExceptionMessage(defaultMessage = "Fail to read the project")
    @PreAuthorize("hasAnyAuthority('Manager')")
    public ResponseEntity<ResponseWrapper>readAllProjeectDetais() throws TicketingProjectException {
       List <ProjectDTO> projectDTOs = projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("Project is completed",projectDTOs));
    }


}





















