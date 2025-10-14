🛠️ Технологии

  🔸Kotlin (100% Kotlin, поддержка Coroutines)

  🔸Jetpack Compose

  🔸Ktor Client 

  🔸kotlinx.serialization 

  🔸Coil (AsyncImage для загрузки изображений)

  🔸Navigation Compose (многэкраность)

  🔸MVVM (ViewModel, StateFlow)

  🔸DI: Koin

  🔸Material 3 Theme

  🔸AndroidX Activity/Runtime/Compose Libraries

  🔸Multimodule

🧩 Архитектура

  🔸Domain layer: модели, бизнес-логика, репозитории.

  🔸Data layer: API-клиенты на Ktor, DTO с kotlinx.serialization, мапперы.

  🔸Presentation (UI) layer: ViewModel + Compose-экраны, логика отображения состояния, навигация.

  🔸Navigation через NavHost, аргументы только id/строковые, данные грузятся внутри VM.

  🔸Single Activity: главный вход и “скелет” приложения.

💡 Логика и экраны

  🔸Главный экран — список конференций, сгруппированных по месяцам.
  
  🔸Экран деталей — название, баннер, иконки времени и места (с форматированием дат и дней), кнопка "Регистрация" с открытием сайта.
  
  🔸Отмена/приглушенность — реализована прозрачностью через .alpha(), чип "Отменено".
  
  🔸Сетевые ошибки/стейт — индикация загрузки, ошибки, пустых состояний.
  
  🔸Соблюден KISS: id через маршрут, все маппинги только в одном направлении, никаких глобальных Singletons.
