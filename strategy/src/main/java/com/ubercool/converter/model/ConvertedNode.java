package com.ubercool.converter.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConvertedNode {
  private String name;
  private String type;
}
