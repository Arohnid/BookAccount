# BookAccount

Это Java-приложение, использующее Spring Boot для хранения и получения информации о книгах и авторах с помощью REST API.

---

##
Ключ доступа к Postman-коллекции:
```
https://api.postman.com/collections/38024706-ef4d25bf-07c5-48f7-a756-8fe5e6c2afcb?access_key=PMAT-01JZ69K7EMRB5EV18NGWQKEWDB
```

---

## Описание

Данный проект позволяет:

- Совершать CRUD операции над книгами
- Получать список всех авторов
- Получать автора по ID
- Добавлять нового автора

---			

## Технологии

- Java 17
- Spring Boot
- H2
- Hibernate

---

## Установка

1. Клонируйте репозиторий или скачайте файлы проекта.

2. В IntelliJ IDEA обновите Maven-проект в pom.xml.

---

## Использование

### Запуск приложения

Запустите проект в среде разработки.

Он запустит Tomcat сервер на `http://localhost:8080/` по умолчанию. 

При каждом запуске в базу данных добавляются две книги и два автора.

Запросы написаны в /src/main/resources/data.sql

```
INSERT INTO authors(name, birth_year) VALUES('J. R. R. Tolkien', 1892);
INSERT INTO authors(name, birth_year) VALUES('Arthur C. Clarke', 1917);
INSERT INTO books(title, author_id, publ_year, genre) VALUES('The Fellowship of the Ring',1,1954,'Fantasy');
INSERT INTO books(title, author_id, publ_year, genre) VALUES('2001: A Space Odyssey',2,1968,'Sci-Fi');
```

### Получение данных

Для получения книг из базы данных, достаточно перейти на `http://localhost:8080/books`.

Выполнится GET-запрос, в результате будет выведен список книг, добавленных в базу данных.

Пример ответа:

```
[
  {
    "id": 1,
    "title": "The Fellowship of the Ring",
    "authorId": 1,
    "year": 1954,
    "genre": "Fantasy"
  },
  {
    "id": 2,
    "title": "2001: A Space Odyssey",
    "authorId": 2,
    "year": 1968,
    "genre": "Sci-Fi"
  }
]
```

Для более удобного использования приложения выше указан ключ доступа к Postman-коллекции.

## Основные функции Books

- `GET http://localhost:8080/books`: Получает список всех книг
- `GET http://localhost:8080/books/{id})`: Получает книгу по ID
- `POST http://localhost:8080/books`: Добавляет книгу

Пример тела запроса в Postman:

<img width="646" height="261" alt="{31A87E58-F531-440E-A1A5-CEFA56A7CBF0}" src="https://github.com/user-attachments/assets/c2e6b743-e7d6-49d0-a3b4-147cb208a010" />

- `PUT http://localhost:8080/books/{id}`: Обновляет книгу с указанным ID
  
Тело запроса такое же, как при добавлении книги.


- `DELETE  http://localhost:8080/books/{id}`: Удаляет книгу с указанным ID

---

## Основные функции Authors

- `GET http://localhost:8080/authors`: Получает список всех авторов
- `GET http://localhost:8080/authors/{id})`: Получает автора по ID
- `POST http://localhost:8080/authors`: Добавляет автора

Пример тела запроса в PostMan:

<img width="640" height="231" alt="{4E64BFB7-BD3D-4CB0-A256-29E566085D3A}" src="https://github.com/user-attachments/assets/7623555f-02fe-49ea-b9ea-e5e971cc6301" />

---

## Замечания

- В приложении присутствует валидация данных при добавлении и обновлении данных
- В проекте написаны тесты для контроллеров и сервисов
