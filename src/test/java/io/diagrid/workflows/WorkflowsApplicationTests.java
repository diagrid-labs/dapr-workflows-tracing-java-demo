package io.diagrid.workflows;

import io.dapr.spring.boot4.autoconfigure.client.DaprClientSB4AutoConfiguration;
import io.dapr.springboot.DaprAutoConfiguration;
import io.dapr.testcontainers.DaprContainer;
import io.dapr.testcontainers.wait.strategy.DaprWait;
import io.diagrid.workflows.services.CounterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = {TestWorkflowsApplication.class, DaprTestContainersConfig.class,
                    DaprAutoConfiguration.class},
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WorkflowsApplicationTests {


  @LocalServerPort
  private int port;

  @Autowired
  private CounterService counterService;

  @Autowired
  private TestSubscriberRestController subscriberController;

  @Autowired
  private DaprContainer daprContainer;


  RestTestClient client;

  @BeforeEach
  void setUp() {
    client = RestTestClient.bindToServer()
        .baseUrl("http://localhost:" + port)
        .build();
    org.testcontainers.Testcontainers.exposeHostPorts(8080);
  }


  @Test
	void testSimpleWorkflow () {

    int counterBeforeWorkflow = counterService.getCounter();
    WorkflowPayload payload = new WorkflowPayload(null, null, "test content");

    EntityExchangeResult<String> instanceId = client.post()
        .uri("/start")
        .contentType(MediaType.APPLICATION_JSON)
        .body(payload)
        .exchange()
        .expectStatus().isOk().returnResult(String.class);

    assertNotNull(instanceId.getResponseBody());

    // Check that the workflow completed successfully
    await().atMost(Duration.ofSeconds(30))
        .pollDelay(500, TimeUnit.MILLISECONDS)
        .pollInterval(500, TimeUnit.MILLISECONDS)
        .until(() -> {
          return counterService.getCounter() > counterBeforeWorkflow;
        });

	}

  @Test
  void testSimpleWorkflowWithoutContent() {

    int counterBeforeWorkflow = counterService.getCounter();
    WorkflowPayload payload = new WorkflowPayload(null, null, null);

    EntityExchangeResult<String> instanceId = client.post()
        .uri("/start")
        .contentType(MediaType.APPLICATION_JSON)
        .body(payload)
        .exchange()
        .expectStatus().isOk().returnResult(String.class);

    assertNotNull(instanceId.getResponseBody());

    // Check that the workflow completed successfully
    await().atMost(Duration.ofSeconds(30))
        .pollDelay(500, TimeUnit.MILLISECONDS)
        .pollInterval(500, TimeUnit.MILLISECONDS)
        .until(() -> {
          return counterService.getCounter() > counterBeforeWorkflow;
        });

  }


}
