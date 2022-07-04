import kafka.KafkaConfig
import kafka.KafkaConsumer
import kafka.ReConsumerStrategy
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.realestate.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.realestate.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.realestate.api.v1.models.*
import java.util.*
import kotlin.test.assertEquals

class KafkaTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = KafkaConfig()
        val inputTopic = config.kafkaTopicIn
        val outputTopic = config.kafkaTopicOut

        val app = KafkaConsumer(config, listOf(ReConsumerStrategy()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        AdCreateRequest(
                        requestId = "11111111-1111-1111-1111-111111111111",
                        ad = AdCreateObject(
                            title = "Some Ad",
                            description = "some testing ad to check them all",
                        ),
                        debug = AdDebug(
                            mode = AdRequestDebugMode.STUB,
                            stub = AdRequestDebugStubs.SUCCESS
                        )
                    )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<AdCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("11111111-1111-1111-1111-111111111111", result.requestId)
        assertEquals("Some Ad", result.ad?.title)
    }

    companion object {
        const val PARTITION = 0
    }

}