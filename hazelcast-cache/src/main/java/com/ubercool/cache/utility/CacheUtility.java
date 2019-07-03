package com.ubercool.cache.utility;

import com.hazelcast.core.HazelcastInstance;
import com.ubercool.cache.model.Operation;
import com.ubercool.cache.model.Task;
import com.ubercool.cache.model.Workflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

@Component
public class CacheUtility {

  @Autowired
  private HazelcastInstance hzInstance;

  @Value("#{cacheManager.getCache('operations')}")
  private Cache operations;

  @Value("#{cacheManager.getCache('tasks')}")
  private Cache tasks;

  @Value("#{cacheManager.getCache('workflows')}")
  private Cache workflows;

  public Task getTaskFromCache(String taskId) {
    return tasks.get(taskId, Task.class);
  }

  public void addTaskToCache(Task task){
    tasks.put(task.getId(), task);
  }

  public Workflow getWorkflowFromCache(String workflowId) {
    return workflows.get(workflowId, Workflow.class);
  }

  public void addWorkflowToCache(Workflow workflow){
    workflows.put(workflow.getId(), workflow);
  }

  public Operation getOperationFromCache(String operationId) {
    return operations.get(operationId, Operation.class);
  }

  public void addOperationToCache(Operation operation){
    operations.put(operation.getId(), operation);
  }
}
