package cloud.cholewa.gateway.rabbit;

import cloud.cholewa.gateway.model.TemperatureMessage;
import cloud.cholewa.home.model.RoomName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TemperaturePublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TemperaturePublisher sut;

    @Test
    void should_publish_temperature_message() {
        // given
        double temperature = 22.5;
        RoomName room = RoomName.LIVING_ROOM;

        // when
        sut.publish(temperature, room)
            .as(StepVerifier::create)
            .verifyComplete();

        // then
        verify(rabbitTemplate).convertAndSend(
            eq("temperature.events"),
            eq(""),
            any(TemperatureMessage.class)
        );
    }
}
