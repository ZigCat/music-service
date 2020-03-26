# Хамбар!!! Обратите внимание на картинку **troubleshoot.png**. В ней я показал, что будет ->

если вместо `AuthorController.authorService.getById(music.getAuthor().getId())` написать `music.getAuthor()`.
похоже, проблема в считывании с базы?


# Music Service - Сервис для подбора и прослушивания музыки

В этой версии добавил в git базу данных, коллекцию Postman с запросами. **Ошибки не исправлены!**

Сервис использует протокол HTTP и формат JSON для передачи данных, авторизация Basic Auth.

### Сервис имеет интерфейс для пользователя:
> - создание аккаунта(**администратора** придется добавлять вручную в базу, поскольку сервер будет выдавать ошибку)
> - добавление музыки пользователю.
>
>
> CRUD пользователя:
>> - `localhost:34567/user/` (**get**) - возвращает всех пользователей
>> - `localhost:34567/user/:id` (**get**) - возвращает пользователя с id
>> - `localhost:34567/user/` (**post**) - создает нового пользователя(**запретит создание администратора**)
>> - `localhost:34567/user/` (**patch**) - редактирует данные пользователя(**администратор *не* может редактировать других**)
>> - `localhost:34567/user/` (**delete**) - удаляет пользователя из базы данных
> 
> Добавления аудиозаписей пользователю.
> **Важно**: действия со своими аудиозаписями может проводить *ТОЛЬКО* авторизованный пользователь
>> - `localhost:34567/audio/` (**get**) - возвращает все аудиозаписи пользователя
>> - `localhost:34567/audio/:id` (**get**) - возвращает аудиозапись позьзователя с id
>> - `localhost:34567/audio/` (**post**) - добавление аудиозаписи пользователю
>> - `localhost:34567/audio/` (**delete**) - удаление аудиозаписи из списка пользователя

### Интерфейс администратора:
> - работа с базой музыки, альбомов, исполнителей, жанров
>
> CRUD музыки, альбомов, исполнителей, групп, жанров идентичен CRUD'у пользователя с различием в наименовании

### Свойства энтити базы: 
- **user**:
	- *id*
	- *name*
	- *surname*
	- *email*
	- *password*
	- *regDate*(registration date)
	- *role*
- **genre**:
	- *id*
	- *name*
- **group**:
	- *id*
	- *name*
	- *creationDate*
	- *dateOfDestroy*
- **author**:
	- *id*
	- *name*
	- *birthday*
	- *dateOfDeath*
	- *description*
	- *group*
- **album**:
	- *id*
	- *name*
	- *creationDate*
	- *author*(must be **null** if group **isn't null**)
	- *group*(must be **null** if author **isn't null**)
- **music**:
	- *id*
	- *name*
	- *genre*
	- *author*(must be **null** if group **isn't null**)
	- *group*(must be **null** if author **isn't null**)
	- *album*
	- *creationDate*
	- *content*(must be stored as **path to file**)
- **userMusic**(relation of music and user):
	- *id*
	- *user*
	- *music*

#### Проект на стадии разработки, в последствии будет добавляться новый функционал
