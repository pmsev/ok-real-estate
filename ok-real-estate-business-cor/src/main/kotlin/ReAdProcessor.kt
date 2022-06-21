import common.initStatus
import common.operation
import models.ReAdId
import models.ReCommand
import stubs.*
import validation.*

class ReAdProcessor {

    suspend fun exec(ctx: ReContext) = businessChain.exec(ctx)

    companion object {
        private const val TITLE_FIELD_NAME = "title"
        private const val DESCRIPTION_FIELD_NAME = "description"
        private const val ID_FIELD_NAME = "id"

        private val businessChain = rootChain<ReContext> {
            initStatus("Инициализация статуса")

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
                    validateFieldNotEmpty("Проверка на непустой id", ID_FIELD_NAME)
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
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
                    validateFieldNotEmpty("Проверка на непустой id", ID_FIELD_NAME)
                    validateIdProperFormat("Проверка формата id")
                    validateFieldNotEmpty("Проверка на непустой заголовок", TITLE_FIELD_NAME)
                    validateFieldHasContent("Проверка на наличие содержания в заголовке", TITLE_FIELD_NAME)
                    validateFieldNotEmpty("Проверка на непустое описание", DESCRIPTION_FIELD_NAME)
                    validateFieldHasContent("Проверка на наличие содержания в описании", DESCRIPTION_FIELD_NAME)
                    validateAdIntObjectNotEmpty("Проверка на наличие сведений об объекте недвижимости")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
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
                    validateFieldNotEmpty("Проверка на непустой id", ID_FIELD_NAME)
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
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
            }

        }.build()

    }

}

