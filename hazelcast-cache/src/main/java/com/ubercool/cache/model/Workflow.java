package com.ubercool.cache.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Workflow implements Serializable {
  private String id;
  private List<Task> tasks;

}
