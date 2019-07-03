package com.ubercool.cache.model;

import java.io.Serializable;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task implements Serializable {
 private String id;
 private Map<String, String> input;
}
