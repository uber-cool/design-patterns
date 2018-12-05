package convert;

import com.ubercool.converter.ConvertNodeImpl;
import com.ubercool.converter.config.AppConfig;
import com.ubercool.converter.model.ConvertedNode;
import com.ubercool.converter.model.Node;

import static com.ubercool.converter.model.NodeType.CP;
import static com.ubercool.converter.model.NodeType.VDU;
import static com.ubercool.converter.model.NodeType.VL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class ConvertNodeTest {

  @Autowired
  ConvertNodeImpl convertNode;

  @Test
  public void convertCPNodeType() {
    Node node = Node.builder()
        .name("CPNode")
        .type(CP).build();
    ConvertedNode convertedNode = convertNode.convertNode(node);
    assert convertedNode.getName().equals("CPNode_CP");
  }

  @Test
  public void convertVLNodeType() {
    Node node = Node.builder()
        .name("VLNode")
        .type(VL).build();
    ConvertedNode convertedNode = convertNode.convertNode(node);
    assert convertedNode.getName().equals("VLNode_VL");
  }

  @Test
  public void convertVDUNodeType() {
    Node node = Node.builder()
        .name("VDUNode")
        .type(VDU).build();
    ConvertedNode convertedNode = convertNode.convertNode(node);
    assert convertedNode.getName().equals("VDUNode_VDU");
  }
}
