# KeyForge — магазин кастомних комп'ютерних аксесуарів

Лабораторна робота №4. Kotlin Multiplatform + Compose Multiplatform: Android, iOS, Desktop (JVM), Web (Wasm / JS).

Дизайн «Retro Keycap»: кремовий фон, іржаво-помаранчевий, гірчичний і бірюзовий акценти, товсті контури та «кейкапні» тіні під кнопками.

## Що зроблено за завданням

| # | Вимога | Де в проєкті |
|---|--------|--------------|
| 1 | Новий KMP-проєкт | цей репозиторій (`androidApp`, `iosApp`, `desktopApp`, `webApp`, `shared`) |
| 2 | Дизайн за допомогою ШІ | макети двох варіантів (Dark Gaming та Retro Keycap), обрано Retro |
| 3 | Кастомна тема (кольори, шрифти) | [`presentation/theme`](shared/src/commonMain/kotlin/ua/chnu/kmp/presentation/theme) — `KeyForgeTheme`, світла й темна палітри, шрифти Rubik / Rubik Mono One / IBM Plex Mono у [`composeResources/font`](shared/src/commonMain/composeResources/font) |
| 4 | Kermit + multiplatform-settings | логування з тегами (`Preferences`, `Cart`, `Catalog`, `Navigation`, `Ktor`…), налаштування в [`AppPreferencesRepository`](shared/src/commonMain/kotlin/ua/chnu/kmp/data/settings/AppPreferencesRepository.kt): мова, тема, сповіщення, чи пройдено онбординг |
| 5 | Онбординг на `Pager` (3–5 слайдів) | [`OnboardingScreen`](shared/src/commonMain/kotlin/ua/chnu/kmp/presentation/onboarding/OnboardingScreen.kt) — `HorizontalPager` на 4 слайди з індикатором, «Далі / Назад / Пропустити» |
| 6 | Локалізація UA / EN | [`values/strings.xml`](shared/src/commonMain/composeResources/values/strings.xml) і [`values-uk/strings.xml`](shared/src/commonMain/composeResources/values-uk/strings.xml); мову можна змінити прямо в застосунку ([`LocalAppLocale`](shared/src/commonMain/kotlin/ua/chnu/kmp/presentation/localization/AppLocale.kt), expect/actual для кожної платформи) |

## Екрани

- **Онбординг** — 4 слайди, перемикач UA/EN; показується лише при першому запуску.
- **Каталог** — пошук, фільтр за категоріями, банер конструктора, картки товарів.
- **Конструктор** — колорвей, світчі, профіль кейкапів, гравіювання, живе прев'ю клавіатури та ціна.
- **Кошик** — список, видалення, сума, оформлення замовлення.
- **Налаштування** — мова, тема (темна / світла / системна), сповіщення, «показати ознайомлення знову».

Каталог поки локальний (`LocalProductRepository`), ціни — приклади. Мережевий шар (Ktor, dummyjson) з попередньої лабораторної залишено для наступних робіт.

## Запуск

- Android: `./gradlew :androidApp:assembleDebug`
- Desktop: `./gradlew :desktopApp:run`
- Web (Wasm): `./gradlew :webApp:wasmJsBrowserDevelopmentRun`
- Web (JS): `./gradlew :webApp:jsBrowserDevelopmentRun`
- iOS: відкрити [`/iosApp`](./iosApp) у Xcode

Тести: `./gradlew :shared:jvmTest`

Шрифти — Google Fonts, ліцензія SIL Open Font License.

Фото товарів надав автор проєкту (архів `pictures.rar`), обрізані під картку каталогу 640×500 і лежать у [`composeResources/drawable`](shared/src/commonMain/composeResources/drawable) як `product_*.jpg`.
