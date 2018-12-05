package com.ubercool.converter.strategies;

import com.ubercool.converter.model.ConvertedNode;
import com.ubercool.converter.model.Node;
import com.ubercool.converter.model.NodeType;

public interface ConverterStrategy {
  ConvertedNode convert(Node node);
  NodeType applicableFor();
}
