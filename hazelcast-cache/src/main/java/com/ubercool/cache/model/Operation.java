package com.ubercool.cache.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Operation implements Serializable {
  private String id;
  private Workflow workflow;
}
