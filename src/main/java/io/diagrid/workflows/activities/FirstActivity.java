package io.diagrid.workflows.activities;

import io.dapr.workflows.WorkflowActivity;
import io.dapr.workflows.WorkflowActivityContext;
import io.diagrid.workflows.WorkflowPayload;
import io.diagrid.workflows.services.CounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirstActivity implements WorkflowActivity {
  private final Logger logger = LoggerFactory.getLogger(FirstActivity.class);

  @Autowired
  private CounterService counterService;

  @Override
  public Object run(WorkflowActivityContext ctx) {
    logger.info("Executing the First activity.");
    WorkflowPayload payload = ctx.getInput(WorkflowPayload.class);
    counterService.incrementCounter();
    payload.getExecutedActivities().add("FirstActivity");
    return payload;
  }
}