package es.digio.sample.webfluxchatdemo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Splitter;

import es.digio.sample.webfluxchatdemo.model.Message;
import es.digio.sample.webfluxchatdemo.repository.MessageRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MessageController {

	@Autowired
	private MessageRepository messageRepository;
	
	@GetMapping("/messages")
    public Flux<Message> getAllMessages() {
        return messageRepository.findAll();
    }
	
	@PostMapping("/insert-message")
    public Mono<Message> createMessage(@RequestBody String postPayload) {
		Map<String, String> parameters = Splitter.on(",").withKeyValueSeparator("=").split(postPayload);
        return messageRepository.save(new Message(parameters.get("message-text")));
    }

    @GetMapping("/messages/{id}")
    public Mono<ResponseEntity<Message>> findMessageById(@PathVariable(value = "id") String messageId) {
        return messageRepository.findById(messageId)
                .map(savedMessage -> ResponseEntity.ok(savedMessage))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // messages are Sent to the client as Server Sent Events
    @GetMapping(value = "/stream/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamAllMessages() {
        return messageRepository.findAll();
    }
}
