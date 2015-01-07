package net.stackoverflow.patterns.numerics.counters;

import net.stackoverflow.util.FilterJunk;
import net.stackoverflow.util.UsersTupleGeneratorFunction;

import org.junit.Test;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class CountingWithCounters {

  public void display_counter_data_on_console() {

    // /// steps ////
    // scrub the data for noise
    // pass it to the tuple generator function
    // either write a new function and use counters in the same OR
    // hack the tuple generator function and incorporate the counter there
    // guess what i wud do ?? hack the previous :)

    final String dir = System.getProperty("user.dir");
    String inputSource = dir + "/src/main/resources/net/do-not-open-in-eclipse-large-files/Users.xml";

    // check the results here after the code is executed
    String sinkFolder = dir + "/src/main/resources/net/results";

    Scheme inputScheme = new TextLine(new Fields("line")); // "line" ??? this is default given by cascading api
    Tap inTap = new Hfs(inputScheme, inputSource);
    Tap sink = new Hfs(new TextLine(), sinkFolder, SinkMode.REPLACE);

    Pipe pipe = new Pipe("copy-pipe");
    pipe = new Each(pipe, new FilterJunk()); // remove xml processing text

    // same sequence as the tuple that wud be generated after this function
    Fields fields = new Fields("Id", "aboutMe", "age", "creationDate", "displayName", "downVotes", "emailHash",
        "lastAccessDate", "location", "reputation", "upVotes", "views", "websiteUrl");

    pipe = new Each(pipe, new UsersTupleGeneratorFunction(fields));

    FlowDef flowDef = new FlowDef();
    flowDef.addSource(pipe, inTap);
    flowDef.addTailSink(pipe, sink);

    Flow<?> flow = new HadoopFlowConnector().connect(flowDef);
    flow.complete();

    // display all counters
    for (String group : flow.getStats().getCounterGroups()) {
      // System.out.println(group);
    }

    // display what we are looking for

    System.out.println("total count of all users who have reputation between 0-100:"
        + flow.getStats().getCounterValue(ReputationCounter.COUNTER_1));
    System.out.println("total count of all users who have reputation between 101-500:"
        + flow.getStats().getCounterValue(ReputationCounter.COUNTER_2));
    System.out.println("total count of all users who have reputation over 500:"
        + flow.getStats().getCounterValue(ReputationCounter.COUNTER_3));

  }

  @Test
  public void runCode() {
    display_counter_data_on_console();
  }

}
