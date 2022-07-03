package kafka

import ReContext
import fromTransport
import ru.otus.otuskotlin.realestate.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.realestate.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.realestate.api.v1.models.Request
import ru.otus.otuskotlin.realestate.api.v1.models.Response
import toTransportAd

class ReConsumerStrategy: ConsumerStrategy {
    override fun topics(config: KafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicIn, config.kafkaTopicOut)
    }

    override fun serialize(source: ReContext): String {
        val response: Response = source.toTransportAd()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: ReContext) {
        val request: Request = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}