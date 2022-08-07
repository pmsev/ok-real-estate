package validation

import ReAdProcessor
import ReAdRepoStub
import models.ReCommand
import models.ReSettings
import org.junit.Test


class BusinessValidationCreateTest {

    private val command = ReCommand.CREATE
    private val settings by lazy {
        ReSettings(
            repoTest = ReAdRepoStub()
        )
    }

    private val processor  by lazy {ReAdProcessor(settings)}

    @Test
    fun correctTitle() = validationTitleCorrect(command, processor)

    @Test
    fun trimTitle() = validationTitleTrim(command, processor)

    @Test
    fun emptyTitle() = validationTitleEmpty(command, processor)

    @Test
    fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test
    fun correctDescription() = validationDescriptionCorrect(command, processor)

    @Test
    fun trimDescription() = validationDescriptionTrim(command, processor)

    @Test
    fun emptyDescription() = validationDescriptionEmpty(command, processor)

    @Test
    fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)
    @Test
    fun emptyReIntObject() = validationEmptyObject(command, processor)
    @Test
    fun nonEmptyReIntObject() = validationNonEmptyObject(command, processor)

}