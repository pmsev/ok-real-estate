package kafka

fun main() {
    val config = KafkaConfig()
    val consumer = KafkaConsumer(config, listOf(ReConsumerStrategy()))
    consumer.run()
}
