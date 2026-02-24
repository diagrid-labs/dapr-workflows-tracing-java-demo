package io.diagrid.workflows;

import java.util.ArrayList;
import java.util.List;

public class WorkflowPayload {

  private String workflowId;
  private String workflowName;
  private String content;
  private List<String> executedActivities;

  public WorkflowPayload() {
    this.executedActivities = new ArrayList<>();
  }

  public WorkflowPayload(String workflowId, String workflowName, String content) {
    this.workflowId = workflowId;
    this.workflowName = workflowName;
    this.content = content;
    this.executedActivities = new ArrayList<>();
  }

  public String getWorkflowId() {
    return workflowId;
  }

  public void setWorkflowId(String workflowId) {
    this.workflowId = workflowId;
  }

  public String getWorkflowName() {
    return workflowName;
  }

  public void setWorkflowName(String workflowName) {
    this.workflowName = workflowName;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<String> getExecutedActivities() {
    return executedActivities;
  }

  public void setExecutedActivities(List<String> executedActivities) {
    this.executedActivities = executedActivities;
  }

  @Override
  public String toString() {
    return "WorkflowPayload{workflowId='" + workflowId + "', workflowName='" + workflowName +
        "', content='" + content + "', executedActivities=" + executedActivities + "}";
  }
}