package com.ubercool.converter.strategies;

import com.ubercool.converter.model.ConvertedNode;
import com.ubercool.converter.model.Node;
import com.ubercool.converter.model.NodeType;
import org.springframework.stereotype.Component;

@Component
public class VDUConverterStrategy implements ConverterStrategy {

  @Override
  public ConvertedNode convert(Node node) {
    return ConvertedNode.builder()
        .name(node.getName() + "_" + node.getType().toString())
        .type(node.getType().toString()).build();
  }

  @Override
  public NodeType applicableFor() {
    return NodeType.VDU;
  }
}
