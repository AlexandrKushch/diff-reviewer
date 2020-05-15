# MR Reviewer

## Цель
создание проекта, который позволит ревьювить код (диф), имея свою систему прав доступа и не позволяющая выходить за рамки посланного дифф файла (существующие системы, имея доступ к МР, можно узнать полное содержимое файла, директории, а это недопустимо в нашем случае, так как студент может еще не выполнить то задание, которое автор сделал перед текущим)

## Детализация задания

### Auth
Имеются следующие роли пользователей:

- Administrator
- Student

Рекомендуется использовать для авторизации систему keycloack

### Core. Create entity
Студент может создать запрос на ревью своего дифа, для этого он указывает следующие данные:

- Ссылку на МР Гитлаба & Gitea (должна быть поддержка обоих систем)

Как только запрос был создан, формируется список ревьюверов по правилу: «только те, кто данный таск уже выполнил». Всем ревьюверам и админам оправляется письмо с уведомлением с ссылкой на созданный запрос. 

### Core. View entity
Запрос могут смотреть только ревьюверы, овнер и админы. Всем остальным должен вернуться 403 код ошибки.

При просмотре запроса, показывается следующая информация:

- Заголовок (из МР)
- Описание (из МР)
- Создатель (из МР)
- Статус мержа (смержен, не смержен, закрыт)
- Номер таска + ссылка на него
- Дифф файл, который создан гитлабом для МР 
- Комментарии

### Core. View entity. Comments
Комментарии характеризуются следующими характеристиками:

- автор
- Файл (путь к файлу). (Выбор из списка)
- Текст комментария (с поддержкой markdawn)
- Статус (открыт, пофикшен, невалиден)

Комментарии,  в том числе статусы, могут изменять и удалять только его овнеры и админы 

### Core. Dashboard
Дашборд:

- Каждый пользователь видит список запросов согласно следующим критериям:
    - Овнер видит все свои запросы
    - Обычный пользователь видит все запросы, в которых он ревьювер
    - Админ видит все запросы
- Каждый запрос характеризуется:
    - Заголовком 
    - Создателем
    - Ссылкой на детализированную информацию
    - Статус (открыт, закрыт, отклонен)

Поставить статус на «закрыт» для запроса может только администратор

Пользователь должен иметь возможность созджать новый МР

## Полезные проекты, которые целесообразно использовать:
- https://github.com/rtfpessoa/diff2html 
- https://github.com/pbu88/diffy 
