package com.ubercool.cache;

import static com.google.common.collect.ImmutableMap.of;
import com.ubercool.cache.model.Workflow;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import com.ubercool.cache.model.Operation;
import com.ubercool.cache.model.Task;
import com.ubercool.cache.model.Workflow;
import com.ubercool.cache.utility.CacheUtility;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HazelcastCacheApplication implements CommandLineRunner {

  @Autowired
  CacheUtility cacheUtility;

  public static void main(String[] args) {
    SpringApplication.run(HazelcastCacheApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    List<Task> tasks = createTasks();
    tasks.forEach(task -> cacheUtility.addTaskToCache(task));

    List<Workflow> workflows = createWorkflows(tasks);
    workflows.forEach(workflow -> cacheUtility.addWorkflowToCache(workflow));

    List<Operation> operations = createOperations(workflows);
    operations.forEach(operation -> cacheUtility.addOperationToCache(operation));
  }

  private List<Operation> createOperations(List<Workflow> workflows) {
    List<Operation> operations = new ArrayList<>();
    operations.add(Operation.builder().id("1").workflow(cacheUtility.getWorkflowFromCache("1")).build());
    operations.add(Operation.builder().id("2").workflow(cacheUtility.getWorkflowFromCache("2")).build());
    operations.add(Operation.builder().id("3").workflow(cacheUtility.getWorkflowFromCache("3")).build());
    return operations;
  }

  private List<Workflow> createWorkflows(List<Task> tasks) {
    List<Workflow> workflows = new ArrayList<>();
    workflows.add(Workflow.builder().id("1").tasks(asList(cacheUtility.getTaskFromCache("1"))).build());
    workflows.add(Workflow.builder().id("2").tasks(
        asList(cacheUtility.getTaskFromCache("2"), cacheUtility.getTaskFromCache("3"))).build());
    workflows.add(Workflow.builder().id("3").tasks(asList(cacheUtility.getTaskFromCache("4"))).build());
    return workflows;
  }

  private List<Task> createTasks() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(Task.builder().id("1").input(asList("input1", "input2").stream().collect(
        toMap(String::toString, String::toString))).build());
    tasks.add(Task.builder().id("2").input(asList("input3", "input4").stream().collect(
        toMap(String::toString, String::toString))).build());
    tasks.add(Task.builder().id("3").input(of("input1", "inputvalue")).build());
    tasks.add(Task.builder().id("4").input(of("input2", "inputvalue")).build());
    return tasks;
  }

}
