package com.ubercool.converter.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Node {
  private String name;
  private NodeType type;

}
