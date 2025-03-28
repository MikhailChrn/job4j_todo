package ru.job4j.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.EditTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.entity.User;
import ru.job4j.service.CategoryService;
import ru.job4j.service.PriorityService;
import ru.job4j.service.TaskService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final CategoryService categoryService;

    private final PriorityService priorityService;

    /**
     * Показать страницу со списком всех задач
     */
    @GetMapping("/all")
    public String getAllTasksByUserId(Model model,
                                      HttpServletRequest request) {

        Optional<User> optionalUser = Optional.of(
                (User) request.getSession().getAttribute("user"));

        model.addAttribute("taskDtos",
                taskService.findAllByUser(optionalUser.get()));
        model.addAttribute("catigories", categoryService.findAll());
        model.addAttribute("pageTitle", "Все задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу со списком новых задач
     */
    @GetMapping("/new")
    public String getNewTasks(Model model,
                              HttpServletRequest request) {

        Optional<User> optionalUser = Optional.of(
                (User) request.getSession().getAttribute("user"));

        model.addAttribute("taskDtos",
                taskService.findAllNewByUser(optionalUser.get()));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("pageTitle", "Новые задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу со списком выполненных задач
     */
    @GetMapping("/completed")
    public String getCompletedTasks(Model model,
                                    HttpServletRequest request) {

        Optional<User> optionalUser = Optional.of(
                (User) request.getSession().getAttribute("user"));

        model.addAttribute("taskDtos",
                taskService.findAllCompletedByUser(optionalUser.get()));
        model.addAttribute("catigories", categoryService.findAll());
        model.addAttribute("pageTitle", "Выполненные задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу для создания новой задачи
     */
    @GetMapping("/create")
    public String getCreationPage(Model model) {

        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());

        return "/tasks/create";
    }

    /**
     * Создать новую задачу из данных пользователя
     */
    @PostMapping("/create")
    public String create(@ModelAttribute CreateTaskDto dto,
                         HttpServletRequest request, Model model) {

        Optional<User> optionalUser = Optional.of(
                (User) request.getSession().getAttribute("user"));
        dto.setUserId(optionalUser.get().getId());

        if (taskService.add(dto)) {
            return "redirect:/tasks/all";
        }

        model.addAttribute("message",
                "Не удалось создать задачу с указанным идентификатором");

        return "/errors/404";
    }

    /**
     * Показать карточку задачи
     */
    @GetMapping("/{id}")
    public String getPageById(@PathVariable int id,
                              Model model) {
        Optional<TaskDto> taskDto = taskService.findById(id);

        if (taskDto.isEmpty()) {
            model.addAttribute("message",
                    "Задача с указанным идентификатором не найдена");
            return "/errors/404";
        }

        model.addAttribute("taskDto", taskDto.get());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());

        return "/tasks/one";
    }

    @GetMapping("/edit/{id}")
    public String getEditPageById(@PathVariable int id,
                                  Model model) {
        Optional<EditTaskDto> editTaskDto = taskService.findEditDtoById(id);

        if (editTaskDto.isEmpty()) {
            model.addAttribute("message",
                    "Задача с указанным идентификатором не найдена");
            return "/errors/404";
        }

        model.addAttribute("editTaskDto", editTaskDto.get());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());

        return "tasks/edit";
    }

    /**
     * Редактировать задачу в соответствии с данными пользователя
     */
    @PostMapping("/edit")
    public String update(@ModelAttribute EditTaskDto editTaskDto,
                         Model model) {

        if (taskService.update(editTaskDto)) {
            return "redirect:/tasks/" + editTaskDto.getId();
        }
        model.addAttribute("message",
                "Не удалось изменить задачу с указанным идентификатором");

        return "/errors/404";
    }

    /**
     * Удалить задачу
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {

        if (taskService.deleteById(id)) {
            return "redirect:/tasks/all";
        }
        model.addAttribute("message",
                "Не удалось удалить задачу с указанным идентификатором");

        return "/errors/404";
    }

    /**
     * Изменить статус задачи
     */
    @GetMapping("/done/{id}")
    public String changeDoneToFalse(@PathVariable int id,
                                    Model model) {

        if (taskService.updateStatusById(id, true)) {
            return "redirect:/tasks/" + id;
        }
        model.addAttribute("message",
                "Не удалось обновить статус задачи");

        return "/errors/404";
    }

}
