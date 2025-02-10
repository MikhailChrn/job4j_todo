## Проект "TODO список".

### Описание задачи:
Цель проекта - разработка веб-приложения мониторинга текущих задач пользователя.

Основной функционал включает:


### Используемые технологии:
+ Java
+ Spring Boot
+ Thymeleaf
+ Bootstrap
+ Hibernate
+ PostgreSQL

### Окружение:
+ Java 17
+ Maven
+ PostgreSQL

### Запуск приложения

1. Создайте базу данных PostgreSQL
``` sql
CREATE USER todo WITH PASSWORD 'todo';
CREATE DATABASE todo
GRANT ALL PRIVILEGES ON DATABASE todo to todo;
```

2. Клонируйте репозиторий
``` bash
cd job4j_todo
git clone https://github.com/MikhailChrn/job4j_todo
```

3. Соберите проект с помощью Maven:
``` bash
mvn clean install 
```

4. Запустите приложение:
``` bash
mvn spring-boot:run
```
5. После запуска:
Проект доступен по адресу: [http://localhost:8080](http://localhost:8080)

### Взаимодействие с приложением

1. Вход

![Вход](screenshots/01_enter.png)

2. Главная

![Главная](screenshots/02_main.png)



### Контакты

mikhail.cherneyev@yandex.ru
