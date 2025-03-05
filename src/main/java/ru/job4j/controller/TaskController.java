package ru.job4j.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.entity.User;
import ru.job4j.service.TaskService;
import ru.job4j.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final UserService userService;

    private final TaskService taskService;

    /**
     * Показать страницу со списком всех задач
     */
    @GetMapping("/all")
    public String getAllTasksByUserId(Model model,
                                      HttpServletRequest request) {
        HttpSession session = request.getSession();
        Optional<User> optionalUser = Optional.of(
                (User) session.getAttribute("user")
        );

        model.addAttribute("taskDtos",
                taskService.findAllByUserId(optionalUser.get().getId()));
        model.addAttribute("pageTitle", "Все задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу со списком новых задач
     */
    @GetMapping("/new")
    public String getNewTasks(Model model,
                              HttpServletRequest request) {
        HttpSession session = request.getSession();
        Optional<User> optionalUser = Optional.of(
                (User) session.getAttribute("user")
        );

        model.addAttribute("taskDtos",
                taskService.findAllNewByUserId(optionalUser.get().getId()));
        model.addAttribute("pageTitle", "Новые задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу со списком выполненных задач
     */
    @GetMapping("/completed")
    public String getCompletedTasks(Model model,
                                    HttpServletRequest request) {
        HttpSession session = request.getSession();
        Optional<User> optionalUser = Optional.of(
                (User) session.getAttribute("user")
        );

        model.addAttribute("taskDtos",
                taskService.findAllCompletedByUserId(optionalUser.get().getId()));
        model.addAttribute("pageTitle", "Выполненные задания");
        return "/tasks/list";
    }

    /**
     * Показать страницу для создания новой задачи
     */
    @GetMapping("/create")
    public String getCreationPage(Model model, HttpServletRequest request) {
        addUserAsAttributeToModel(model, request);
        return "/tasks/create";
    }

    /**
     * Создать новую задачу из данных пользователя
     */
    @PostMapping("/create")
    public String create(@ModelAttribute CreateTaskDto dto,
                         Model model,
                         HttpServletRequest request) {
        HttpSession session = request.getSession();
        Optional<User> optionalUser = Optional.of(
                (User) session.getAttribute("user")
        );
        dto.setUserId(optionalUser.get().getId());

        if (taskService.add(dto)) {
            return "redirect:/tasks/all";
        }

        model.addAttribute("message",
                "Не удалось создать задачу с указанным идентификатором");

        return "/errors/404";

    }

    /**
     * Показать страницу для редактирования задачи
     */
    @GetMapping("/{id}")
    public String getPageById(@PathVariable int id,
                              Model model,
                              HttpServletRequest request) {
        addUserAsAttributeToModel(model, request);
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
    public String update(@ModelAttribute TaskDto taskDto,
                         Model model,
                         HttpServletRequest request) {
        addUserAsAttributeToModel(model, request);

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
    public String delete(@PathVariable int id, Model model,
                         HttpServletRequest request) {
        addUserAsAttributeToModel(model, request);

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
                                    Model model,
                                    HttpServletRequest request) {
        addUserAsAttributeToModel(model, request);

        if (taskService.updateStatusById(id, true)) {
            return "redirect:/tasks/" + id;
        }
        model.addAttribute("message",
                "Не удалось обновить статус задачи");

        return "/errors/404";
    }

    public void addUserAsAttributeToModel(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }

        model.addAttribute("user", user);
    }

}
