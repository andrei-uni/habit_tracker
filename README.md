# Habit Tracker

Разработал android-приложение для трекинга привычек. Привычки хранятся в локальной базе данных и синхронизируются с удаленным сервером.

Использовал подход Clean Architecture для проектирования приложения. Навигация реализована с помощью NavController. Асинхронные запросы выполняются с помощью корутин. Все данные от репозиториев к ViewModel передаются через Flow. Код покрыт unit-тестами и UI-тестами с помощью Kaspresso.

## Стэк
- Okhttp, Retrofit, REST API, JSON
- Room
- Dagger + Hilt
- JUnit, Kaspresso
