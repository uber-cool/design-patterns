package com.ubercool.converter.strategies;

import com.google.common.collect.Maps;
import com.ubercool.converter.model.Node;
import com.ubercool.converter.model.NodeType;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategySelector {

  private Map<NodeType, ConverterStrategy> converterStrategyMap = Maps.newHashMap();

  @Autowired
  public StrategySelector(List<ConverterStrategy> converterStrategies){
    // Add all strategies to the map
    converterStrategies.stream().forEach(converterStrategy ->
        converterStrategyMap.put(converterStrategy.applicableFor(), converterStrategy));
  }

  public ConverterStrategy selectStrategy(Node node) {
    // Select applicable strategy, can be more complex or based on other criteria
    return converterStrategyMap.get(node.getType());
  }
}
