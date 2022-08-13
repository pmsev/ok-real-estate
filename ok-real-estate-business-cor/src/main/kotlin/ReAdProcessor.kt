import common.*
import models.ReAdId
import models.ReCommand
import models.ReSettings
import models.ReState
import permissions.accessValidation
import permissions.chainPermissions
import permissions.frontPermissions
import permissions.searchTypes
import repo.*
import stubs.*
import validation.*

class ReAdProcessor(private val settings: ReSettings = ReSettings()) {

    suspend fun exec(ctx: ReContext) = businessChain.exec(ctx.apply { settings = this@ReAdProcessor.settings })

    companion object {
        private const val TITLE_FIELD_NAME = "title"
        private const val DESCRIPTION_FIELD_NAME = "description"

        private val businessChain = rootChain<ReContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

            operation("Создание объявления", ReCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }

                    validateFieldNotEmpty("Проверка на непустой заголовок", TITLE_FIELD_NAME)
                    validateFieldHasContent("Проверка на наличие содержания в заголовке", TITLE_FIELD_NAME)
                    validateFieldNotEmpty("Проверка на непустое описание", DESCRIPTION_FIELD_NAME)
                    validateFieldHasContent("Проверка на наличие содержания в описании", DESCRIPTION_FIELD_NAME)
                    validateAdIntObjectNotEmpty("Проверка на наличие сведений об объекте недвижимости")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                worker {
                    title = "Инициализация adRepoRead"
                    on { state == ReState.RUNNING }
                    handle {
                        adRepoRead = adValidated
                        adRepoRead.sellerId = principal.id
                    }
                }
                accessValidation("Вычисление прав доступа")
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    repoCreate("Создание объявления в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Получить объявление", ReCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = ReAdId(adValidating.id.asString().trim()) }
                    validateIdIsNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == ReState.RUNNING }
                        handle { adRepoDone = adRepoRead }
                    }
                }

                prepareResult("Подготовка ответа")
            }
            operation("Изменить объявление", ReCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = ReAdId(adValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                    validateIdIsNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateFieldNotEmpty("Проверка на непустой заголовок", TITLE_FIELD_NAME)
                    validateFieldHasContent("Проверка на наличие содержания в заголовке", TITLE_FIELD_NAME)
                    validateFieldNotEmpty("Проверка на непустое описание", DESCRIPTION_FIELD_NAME)
                    validateFieldHasContent("Проверка на наличие содержания в описании", DESCRIPTION_FIELD_NAME)
                    validateAdIntObjectNotEmpty("Проверка на наличие сведений об объекте недвижимости")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    repoCheckReadLock("Проверяем блокировку")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление объявления в БД")
                }

                prepareResult("Подготовка ответа")
            }
            operation("Удалить объявление", ReCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = ReAdId(adValidating.id.asString().trim()) }
                    validateIdIsNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    repoCheckReadLock("Проверяем блокировку")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление объявления из БД")
                }

                prepareResult("Подготовка ответа")
            }
            operation("Поиск объявлений", ReCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adFilterValidating") { adFilterValidating = adFilterRequest.copy() }

                    finishAdFilterValidation("Успешное завершение процедуры валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                searchTypes("Подготовка поискового запроса")
                repoSearch("Поиск объявления в БД по фильтру")
                prepareResult("Подготовка ответа")
            }

        }.build()

    }

}

