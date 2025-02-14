package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yps.systems.ai.model.Objective;
import yps.systems.ai.repository.IObjectiveRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/query/objectiveService")
public class ObjectiveQueryController {

    private final IObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveQueryController(IObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    @GetMapping
    public ResponseEntity<List<Objective>> getPersons() {
        List<Objective> objectives = objectiveRepository.findAll();
        return ResponseEntity.ok(objectives);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Objective> getPersonById(@PathVariable String id) {
        Optional<Objective> optionalObjective = objectiveRepository.findById(id);
        return optionalObjective.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
