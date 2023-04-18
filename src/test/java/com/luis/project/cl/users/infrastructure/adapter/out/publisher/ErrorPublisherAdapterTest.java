package com.luis.project.cl.users.infrastructure.adapter.out.publisher;

import com.luis.project.cl.users.application.ports.ErrorPublisherPort;
import com.luis.project.cl.users.domain.constant.Constants;
import com.luis.project.cl.users.domain.pubsubpublisher.ErrorPubSub;
import com.google.api.core.SettableApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrorPublisherAdapterTest {

    @Mock
    private Publisher publisher;
    private ErrorPublisherPort errorPublisherPort;
    private SettableApiFuture<String> settableApiFuture;

    @BeforeEach
    void setUp() {
        errorPublisherPort = new ErrorPublisherAdapter(publisher);
        this.settableApiFuture = SettableApiFuture.create();
        MDC.setContextMap(new HashMap<>());
    }

    @Test
    void publishMessageSuccessfully() {
        // Preparing data
        ErrorPubSub message = ErrorPubSub.builder()
                .error("Error description")
                .traceId(UUID.randomUUID()
                        .toString())
                .build();
        MDC.put(Constants.COUNTRY_REF, "CL");
        MDC.put(Constants.COMMERCE_REF, "IKS");
        MDC.put(Constants.CHANNEL_REF, "channel");
        // Mocks & Stubs configuration
        this.settableApiFuture.set("result");
        when(publisher.publish(any())).thenReturn(this.settableApiFuture);
        // Business logic execution
        errorPublisherPort.publishMessage(message);
        // Validating mocks behaviour

        verify(publisher, timeout(700)).publish(argThat(msg -> {
            var mapHeader = msg.getAttributesMap();
            return mapHeader.get(Constants.COUNTRY_REF).equals("CL")
                    && mapHeader.get(Constants.COMMERCE_REF).equals("IKS")
                    && mapHeader.get(Constants.CHANNEL_REF).equals("channel");
        }));

    }

    @Test
    void publishMessageFailed() {
        // Preparing data
        ErrorPubSub message = ErrorPubSub.builder()
                .error("Error description")
                .traceId(UUID.randomUUID()
                        .toString())
                .build();
        // Mocks & Stubs configuration
        this.settableApiFuture.setException(new Throwable());
        when(publisher.publish(any())).thenReturn(this.settableApiFuture);
        // Business logic execution
        errorPublisherPort.publishMessage(message);
        // Validating mocks behaviour
        verify(publisher, timeout(500)).publish(any());
    }
}