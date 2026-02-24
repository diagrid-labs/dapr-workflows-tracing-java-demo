package io.diagrid.workflows;

import io.dapr.durabletask.Task;
import io.dapr.workflows.Workflow;
import io.dapr.workflows.WorkflowStub;
import io.diagrid.workflows.activities.ConditionalActivity;
import io.diagrid.workflows.activities.FirstActivity;
import io.diagrid.workflows.activities.SecondActivity;
import io.diagrid.workflows.activities.SequentialActivity;
import io.diagrid.workflows.activities.ThirdActivity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleWorkflow implements Workflow {
  @Override
  public WorkflowStub create() {
    return ctx -> {
      WorkflowPayload payload = ctx.getInput(WorkflowPayload.class);
      payload.setWorkflowId(ctx.getInstanceId());

      // Parallel activities: FirstActivity, SecondActivity, and ThirdActivity run concurrently
      Task<WorkflowPayload> first = ctx.callActivity(FirstActivity.class.getName(), payload, WorkflowPayload.class);
      Task<WorkflowPayload> second = ctx.callActivity(SecondActivity.class.getName(), payload, WorkflowPayload.class);
      Task<WorkflowPayload> third = ctx.callActivity(ThirdActivity.class.getName(), payload, WorkflowPayload.class);

      ctx.allOf(List.of(first, second, third)).await();

      // Merge executed activities from all parallel results
      WorkflowPayload merged = new WorkflowPayload(payload.getWorkflowId(), payload.getWorkflowName(), payload.getContent());
      merged.getExecutedActivities().addAll(first.await().getExecutedActivities());
      merged.getExecutedActivities().addAll(second.await().getExecutedActivities());
      merged.getExecutedActivities().addAll(third.await().getExecutedActivities());

      // Sequential activity: runs after all parallel activities complete
      WorkflowPayload afterSequential = ctx.callActivity(SequentialActivity.class.getName(), merged, WorkflowPayload.class).await();

      // Conditional activity: only runs if content is not null and not empty
      if (afterSequential.getContent() != null && !afterSequential.getContent().isEmpty()) {
        afterSequential = ctx.callActivity(ConditionalActivity.class.getName(), afterSequential, WorkflowPayload.class).await();
      }

      ctx.complete(afterSequential);
    };
  }
}