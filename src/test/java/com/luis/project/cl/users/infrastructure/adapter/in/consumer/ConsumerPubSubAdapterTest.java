package com.luis.project.cl.users.infrastructure.adapter.in.consumer;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConsumerPubSubAdapterTest {

    @Mock
    private AckReplyConsumer ackReplyConsumer;
    private ConsumerPubSubAdapter consumerPubSubAdapter;

    @BeforeEach
    void setUp() {
        consumerPubSubAdapter = new ConsumerPubSubAdapter("projectId", "subscriptionId");
    }

    @Test
    void givenEvent_processSuccessfully() {
        // Preparing data
        String payload = "{\n" +
                "    \"businessProvider\": \"SOVOS\",\n" +
                "    \"documentType\": \"CNO\",\n" +
                "    \"documentId\": \"9994\",\n" +
                "    \"documentPrefix\": \"BQ02\",\n" +
                "    \"messageCode\": \"9000174478CNOBQ011\",\n" +
                "    \"businessProviderCode\": \"-1\",\n" +
                "    \"internalErrorCode\": \"\",\n" +
                "    \"errorMessage\": \"Error, archivo XML no valido. Linea 31 Posicion 1, No se informa Tipo Impto\"\n" +
                "}";
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(
                        ByteString.copyFrom(payload, StandardCharsets.UTF_8))
                .build();
        // Mocks & Stubs configuration
        // Business logic execution
        consumerPubSubAdapter.receiveMessage(pubsubMessage, ackReplyConsumer);
        // Validating mocks behaviour
        verify(ackReplyConsumer).ack();
    }
}