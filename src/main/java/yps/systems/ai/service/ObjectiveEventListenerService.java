package yps.systems.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import yps.systems.ai.model.Objective;
import yps.systems.ai.repository.IObjectiveRepository;

import java.util.Optional;

@Service
public class ObjectiveEventListenerService {

    private final IObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveEventListenerService(IObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    @KafkaListener(topics = "${env.kafka.topicEvent}")
    public void listen(@Payload String payload, @Header("eventType") String eventType, @Header("source") String source) {
        System.out.println("Processing " + eventType + " event from " + source);
        switch (eventType) {
            case "CREATE_OBJECTIVE":
                try {
                    Objective objective = new ObjectMapper().readValue(payload, Objective.class);
                    objectiveRepository.save(objective);
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Objective JSON: " + e.getMessage());
                }
                break;
            case "UPDATE_OBJECTIVE":
                try {
                    Objective objective = new ObjectMapper().readValue(payload, Objective.class);
                    Optional<Objective> optionalObjective = objectiveRepository.findById(objective.getId());
                    optionalObjective.ifPresent(existingPerson -> objectiveRepository.save(objective));
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Objective JSON: " + e.getMessage());
                }
                break;
            case "DELETE_OBJECTIVE":
                Optional<Objective> optionalObjective = objectiveRepository.findById(payload.replaceAll("\"", ""));
                optionalObjective.ifPresent(value -> objectiveRepository.deleteById(value.getId()));
                break;
            default:
                System.out.println("Unknown event type: " + eventType);
        }
    }

}
