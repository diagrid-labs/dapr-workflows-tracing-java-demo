package io.diagrid.workflows;

import io.dapr.workflows.client.DaprWorkflowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowsRestController {

  @Autowired
  private DaprWorkflowClient daprWorkflowClient;

  @PostMapping("/start")
  public String startWorkflow(@RequestBody(required = false) WorkflowPayload payload) {
    if (payload == null) {
      payload = new WorkflowPayload();
    }
    payload.setWorkflowName("SimpleWorkflow");
    return daprWorkflowClient.scheduleNewWorkflow(SimpleWorkflow.class, payload);
  }

}