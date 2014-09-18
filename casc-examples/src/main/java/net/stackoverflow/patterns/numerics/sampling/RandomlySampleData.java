package net.stackoverflow.patterns.numerics.sampling;

import net.stackoverflow.util.FilterJunk;

import org.junit.Test;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class RandomlySampleData {


  public void random_sample() {

    final String dir = System.getProperty("user.dir");
    String inputSource = dir + "/src/main/resources/net/do-not-open-in-eclipse-large-files/Users.xml";

    // check the results here after the code is executed
    String sinkFolder = dir + "/src/main/resources/net/results";

    Scheme inputScheme = new TextLine(new Fields("line")); // "line" ??? this is default given by cascading api
    Tap inTap = new Hfs(inputScheme, inputSource);
    Tap sink = new Hfs(new TextLine(), sinkFolder, SinkMode.REPLACE);

    Pipe pipe = new Pipe("copy-pipe");
    pipe = new Each(pipe, new FilterJunk()); // remove xml processing text

    pipe = new Each(pipe, new RandomOperation());
    
    FlowDef flowDef = new FlowDef();
    flowDef.addSource(pipe, inTap);
    flowDef.addTailSink(pipe, sink);
    
    Flow<?> flow = new Hadoop2MR1FlowConnector().connect(flowDef);
    flow.complete();
    



  }

  @Test
  public void runCode() {
    random_sample();
  }

}
