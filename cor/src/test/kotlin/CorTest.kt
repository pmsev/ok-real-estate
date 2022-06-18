import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CorTest {

    @Test
    fun chainSuccessTest() = runTest {
        val ctx = TestContext(counter = 0)
        chain.exec(ctx)
        assertEquals(CorStatus.RUNNING, ctx.status)
        assertEquals(3, ctx.counter)
    }

    @Test
    fun chainFailTest() = runTest {
        val ctx = TestContext(counter = 0)
        chainWithException.exec(ctx)
        assertEquals(CorStatus.FAILING, ctx.status)
        assertEquals(2, ctx.counter)
    }


    companion object {
        val chain = rootChain<TestContext> {
            worker {
                title = "Status initialization"
                description = "Check the status initialization at the beginning"

                on { status == CorStatus.NONE }
                handle { status = CorStatus.RUNNING }

            }

            chain {
                on { status == CorStatus.RUNNING }

                worker(
                    title = "Some worker",
                    description = "Some description"
                ) {
                    counter += 3
                }
            }


        }.build()
        val chainWithException = rootChain<TestContext> {
            worker {
                title = "Status initialization"
                description = "Check the status initialization at the beginning"

                on { status == CorStatus.NONE }
                handle { status = CorStatus.RUNNING }

            }

            chain {
                on { status == CorStatus.RUNNING }
                chain {
                    worker {
                        handle {
                            counter /= 0
                        }
                        exception {
                            status = CorStatus.FAILING
                        }
                    }
                }
                worker(
                    title = "Some worker",
                    description = "Some description"
                ) {
                    counter += 2
                }
            }
        }.build()
    }
}


data class TestContext(
    var status: CorStatus = CorStatus.NONE,
    var counter: Int = Int.MIN_VALUE
)

enum class CorStatus {
    NONE,
    RUNNING,
    FAILING,
}