package com.ubercool.converter;

import com.ubercool.converter.model.ConvertedNode;
import com.ubercool.converter.model.Node;
import com.ubercool.converter.strategies.StrategySelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertNodeImpl {

  @Autowired
  private StrategySelector strategySelector;
  public ConvertedNode convertNode(Node node){
    return strategySelector.selectStrategy(node).convert(node);
  }

}
