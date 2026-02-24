package io.diagrid.workflows;

import io.dapr.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TestSubscriberRestController {

  private final List<Map<String, Object>> events = new ArrayList<>();

  private final Logger logger = LoggerFactory.getLogger(TestSubscriberRestController.class);

  @PostMapping("subscribe")
  @Topic(pubsubName = "pubsub", name = "topic")
  public void subscribe(@RequestBody Map<String, Object> cloudEvent) {
    logger.info("Event Received: " + cloudEvent);
    events.add(cloudEvent);
  }

  public List<Map<String, Object>> getAllEvents() {
    return events;
  }
}