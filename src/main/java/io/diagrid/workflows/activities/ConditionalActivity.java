package io.diagrid.workflows.activities;

import io.dapr.workflows.WorkflowActivity;
import io.dapr.workflows.WorkflowActivityContext;
import io.diagrid.workflows.WorkflowPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConditionalActivity implements WorkflowActivity {
  private final Logger logger = LoggerFactory.getLogger(ConditionalActivity.class);

  @Override
  public Object run(WorkflowActivityContext ctx) {
    logger.info("Executing the Conditional activity.");
    WorkflowPayload payload = ctx.getInput(WorkflowPayload.class);
    payload.getExecutedActivities().add("ConditionalActivity");
    return payload;
  }
}