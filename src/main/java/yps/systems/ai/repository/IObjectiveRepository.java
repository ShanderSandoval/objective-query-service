package yps.systems.ai.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yps.systems.ai.model.Objective;

@Repository
public interface IObjectiveRepository extends MongoRepository<Objective, String> {
}
