# Music Service - Сервис для подбора и прослушивания музыки

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

### Поиск музыки
> Поиск осуществляется через *queryParam* по 4 параметрам(**каждый из них может быть `null`**):
>> - name
>> - author(должен быть `int`)
>> - group(должен быть `int`)
>> - genre(должен быть `int`)
>
> **author и group не могут быть вызваны одновременно**
> 
> URL запроса:
>
> `localhost:34567/search?name=*your value*&group=*your value*&author=*your value*&genre=*your value*`

### Подбор музыки
> Подбор осуществляется через *queryParam*: необходимо выбрать один из 2 видов подбора:
>> - автоподбор(осуществляется по добавленной пользователем музыкой - *search_type = 1*)
>> - ручной подбор(пользователь вручную вводит параметры подбора - *search_type = 1&&genre/music*)
>
> URL запроса:
>
> `localhost:34567/select/?search_type=2&music=1`

### Частые ошибки
- `"Generic 500 message"`| `Internal server error`(ошибка сервера)
- `"Generic 403 message"`(доступ запрещен - не верные параметры BasicAuth)
- `"Generic 400 message"`(ошибка клиента - не верный queryParam, не верные параметры запроса и т.д.)



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
- **AuthorGroup**(relation of author and group):
    - *id*
    - *author*
    - *group*
- **Category**(category of tags):
    - *id*
    - *name*
- **Tag**:
    - *id*
    - *name*
    - *category*
- **TagAlbum**(relation of tag and album):
    - *id*
    - *tag*
    - *album*
- **CategoryGenre**(relation of category and genre):
    - *id*
    - *category*
    - *genre*
