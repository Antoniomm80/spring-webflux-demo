package es.digio.sample.webfluxchatdemo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import es.digio.sample.webfluxchatdemo.model.Message;

public interface MessageRepository extends ReactiveMongoRepository<Message, String>{

}
