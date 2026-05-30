## Лабораторная работа №6 - Доработка программы

### Описание

Данный проект представляет собой многопоточный сервер для управления коллекцией городов с использованием:
- **PostgreSQL** для хранения данных
- **SHA-256** для хеширования паролей
- **Многопоточность** с использованием различных типов thread pools
- **Аутентификация и авторизация** пользователей

### Требования задания и их реализация

#### 1. ✅ Хранение коллекции в PostgreSQL

**Файл:** `server/db/DatabaseManager.kt`

- Подключение к PostgreSQL на хосте `pg`, БД `studs`
- Использование переменных окружения `PGUSER` и `PGPASSWORD`
- Автоматическое создание схемы при инициализации
- Использование `SEQUENCE` для генерации ID городов

```sql
CREATE SEQUENCE city_id_seq START WITH 1
CREATE TABLE cities (
    id BIGINT DEFAULT nextval('city_id_seq') PRIMARY KEY,
    owner_id INT NOT NULL REFERENCES users(id),
    ...
)
```

#### 2. ✅ Обновление коллекции в памяти

**Файл:** `server/Server.kt`, метод `handleAddCommand`

- Коллекция обновляется в памяти только после успешного сохранения в БД
- При добавлении города сначала выполняется INSERT в БД, затем загрузка в `SynchronizedCityCollection`

```kotlin
val cityId = CityRepository.saveCityToDatabase(city)
if (cityId > 0) {
    val savedCity = CityRepository.getCityById(cityId)
    if (savedCity != null) {
        collection.add(savedCity)
    }
}
```

#### 3. ✅ Получение данных из памяти

**Файл:** `server/Server.kt`, метод `handleShowCommand`

- Команды SHOW и INFO работают с коллекцией в памяти
- Команды ADD, UPDATE, DELETE работают с БД и синхронизируют память

```kotlin
private fun handleShowCommand(): String {
    val cities = collection.getAll()  // Читаем из памяти
    ...
}
```

#### 4. ✅ Регистрация и аутентификация пользователей

**Файлы:**
- `server/auth/UserManager.kt` - логика аутентификации/регистрации
- `server/auth/PasswordManager.kt` - хеширование паролей SHA-256

```kotlin
fun register(username: String, password: String): Boolean {
    // Вставка в БД с хешированным паролем
    val hash = PasswordManager.hashPassword(password)
    // INSERT INTO users (username, password_hash)
}
```

#### 5. ✅ Хеширование паролей SHA-256

**Файл:** `server/auth/PasswordManager.kt`

```kotlin
fun hashPassword(password: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(password.toByteArray())
    return hash.joinToString("") { "%02x".format(it) }
}
```

#### 6. ✅ Аутентификация перед каждой командой

**Файл:** `server/Server.kt`, метод `handleClient`

- Клиент отправляет username и password при подключении
- Успешная аутентификация возвращает user ID
- Все последующие команды связаны с этим user ID

#### 7. ✅ Сохранение информации о владельце объекта

**Файл:** `common/City.kt`

```kotlin
data class City(
    val id: Long,
    val owner: Int,  // User ID владельца
    ...
)
```

Каждый город хранит `owner_id` в БД, который связан с пользователем, создавшим его.

#### 8. ✅ Права доступа пользователей

**Файл:** `server/Server.kt`, методы `handleUpdateCommand` и `handleRemoveByIdCommand`

- Пользователи видят все города (SHOW работает со всеми)
- Модифицировать могут только свои города:

```kotlin
if (city.owner != userId) {
    return "You don't have permission to update this city"
}
```

#### 9. ✅ Многопоточная обработка запросов

### Архитектура многопоточности

**Файл:** `server/threading/ThreadPoolManager.kt`

Используется 3 разных thread pool'а согласно требованиям:

```kotlin
// Fixed thread pool (4 потока) для чтения запросов
val readingPool: ExecutorService = Executors.newFixedThreadPool(4)

// Cached thread pool для обработки команд
val processingPool: ExecutorService = Executors.newCachedThreadPool()

// Fixed thread pool (4 потока) для отправки ответов
val sendingPool: ExecutorService = Executors.newFixedThreadPool(4)
```

### Синхронизация доступа к коллекции

**Файл:** `server/collection/SynchronizedCityCollection.kt`

```kotlin
private val cities = Collections.synchronizedSortedMap(sortedMapOf<Long, City>())
```

Используется `Collections.synchronizedSortedMap()` для безопасного многопоточного доступа.

### Структура проекта

```
prog_lab6_server/
├── src/main/kotlin/
│   ├── server/
│   │   ├── Server.kt                    # Главный класс сервера
│   │   ├── db/
│   │   │   └── DatabaseManager.kt       # Управление БД
│   │   ├── auth/
│   │   │   ├── UserManager.kt           # Аутентификация
│   │   │   └── PasswordManager.kt       # Хеширование
│   │   ├── collection/
│   │   │   ├── SynchronizedCityCollection.kt  # Синхронизированная коллекция
│   │   │   └── CityRepository.kt        # Операции с БД
│   │   └── threading/
│   │       └── ThreadPoolManager.kt     # Управление потоками
│   └── common/
│       ├── City.kt                      # Модель города
│       └── Auth.kt                      # Классы аутентификации

prog_lab6_client/
├── src/main/kotlin/
│   └── client/
│       ├── ClientMain.kt                # Интерактивный клиент
│       └── Client.kt                    # Класс клиента
```

### Команды сервера

| Команда | Описание |
|---------|---------|
| `ADD` | Добавить новый город |
| `SHOW` | Показать все города |
| `INFO` | Информация о коллекции |
| `UPDATE <id>` | Обновить город |
| `REMOVE_BY_ID <id>` | Удалить город по ID |
| `CLEAR` | Удалить все города пользователя |
| `HELP` | Справка |
| `EXIT` | Выход |

### Запуск

#### Сервер

```bash
cd prog_lab6_server
./gradlew build
java -jar build/libs/my-project-all.jar [port]
```

#### Клиент

```bash
cd prog_lab6_client
./gradlew build
java -jar build/libs/my-project-all.jar
```

Подключиться к серверу на localhost:9999 и ввести учетные данные.

### Требования к БД

PostgreSQL должен быть запущен на хосте `pg` с:
- Имя БД: `studs`
- Пользователь: `studs` (или из переменной `PGUSER`)
- Пароль: (или из переменной `PGPASSWORD`)

### Основные особенности

1. **Асинхронная обработка**: Каждое клиентское соединение обрабатывается в отдельном потоке
2. **Эффективные thread pools**: Разные пулы для разных этапов обработки
3. **Безопасность**: SHA-256 хеширование, проверка прав доступа
4. **Надежность**: Все операции с БД обрабатывают ошибки
5. **Масштабируемость**: Синхронизированная коллекция поддерживает конкурентный доступ
