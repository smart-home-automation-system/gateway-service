package cloud.cholewa.gateway.rabbit;

import cloud.cholewa.gateway.model.TemperatureMessage;
import cloud.cholewa.home.model.RoomName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperaturePublisher {

    private final RabbitTemplate rabbitTemplate;

    public Mono<Void> publish(final double temperature, final RoomName room) {
        return Mono.fromRunnable(() -> {
                log.debug("Publishing temperature: {} for room: {}", temperature, room);
                rabbitTemplate.convertAndSend("temperature.events", "", new TemperatureMessage(room.getValue(), temperature));
            })
            .subscribeOn(Schedulers.boundedElastic())
            .then();
    }
}
