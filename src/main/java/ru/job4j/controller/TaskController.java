package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.service.TaskService;

import java.util.Optional;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Показать страницу со списком всех задач
     */
    @GetMapping("/all")
    public String getAllTasks(Model model) {
        model.addAttribute("taskDtos", taskService.findAll());
        model.addAttribute("pageTitle", "Все задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу со списком новых задач
     */
    @GetMapping("/new")
    public String getNewTasks(Model model) {
        model.addAttribute("taskDtos", taskService.findAllNew());
        model.addAttribute("pageTitle", "Новые задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу со списком выполненных задач
     */
    @GetMapping("/completed")
    public String getCompletedTasks(Model model) {
        model.addAttribute("taskDtos", taskService.findAllCompleted());
        model.addAttribute("pageTitle", "Выполненные задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу для создания новой задачи
     */
    @GetMapping("/create")
    public String getCreationPage(Model model) {
        return "/tasks/create";
    }

    /**
     * Создать новую задачу из данных пользователя
     */
    @PostMapping("/create")
    public String create(@ModelAttribute CreateTaskDto dto, Model model) {
        if (taskService.add(dto)) {
            return "redirect:/tasks/new";
        }
        model.addAttribute("message",
                "Не удалось создать задачу с указанным идентификатором");

        return "/errors/404";

    }


    /**
     * Показать страницу для редактирования задачи
     */
    @GetMapping("/{id}")
    public String getPageById(@PathVariable int id, Model model) {
        Optional<TaskDto> taskDto = taskService.findById(id);
        if (taskDto.isEmpty()) {
            model.addAttribute("message",
                    "Задача с указанным идентификатором не найдена");
            return "/errors/404";
        }
        model.addAttribute("taskDto", taskDto.get());

        return "/tasks/edit";
    }

    /**
     * Редактировать задачу в соответствии с данными пользователя
     */
    @PostMapping("/edit")
    public String update(@ModelAttribute TaskDto taskDto, Model model) {
        if (taskService.update(taskDto)) {
            return "redirect:/tasks/all";
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
    public String changeDone(@PathVariable int id, Model model) {
        if (taskService.invertDoneById(id)) {
            return "redirect:/tasks/all";
        }
        model.addAttribute("message",
                "Не удалось изменить задачу с указанным идентификатором");

        return "/errors/404";
    }

}
