package com.ubercool.operation.manager.model;

import java.util.List;
import lombok.Data;

@Data
public class OperationContext {
  private String input;
  private List<String> additionalData;
  private String operationId;
}
