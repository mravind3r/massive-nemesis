package net.stackoverflow.patterns.numerics.topn;

import net.stackoverflow.util.FilterJunk;
import net.stackoverflow.util.UsersTupleGeneratorFunction;

import org.junit.Test;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;
import cascading.operation.Identity;
import cascading.pipe.Each;
import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.pipe.assembly.Retain;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class TopNTupleAssembly {

  public void do_as_required() {

    String path = System.getProperty("user.dir");
    String inputPath = path + "/src/main/resources/net/do-not-open-in-eclipse-large-files/Users.xml";

    // check the results here ( visual inspection only, will make a tool avail later)
    // eg: cat ~/src/main/resources/net/results/part-00000
    String outputDir = path + "/src/main/resources/net/results";

    Scheme inputScheme = new TextLine(new Fields("line"));
    // can also be written as Scheme inputScheme = new TextLine(); // Fields("line") is default
    // reason TextLine is used cos , need to clean data of all the noise it has
    // if the input was clearly defined in a f1 f2 f2 pattern, use TextDelimited-- use /t as delimiter

    Tap inTap = new Hfs(inputScheme, inputPath);
    Tap sinkTap = new Hfs(new TextLine(), outputDir, SinkMode.REPLACE); // default tab seperated output
    // make sure the import is either local.Hfs or local.TextLine , mixing with hadoop.TextLine will trip u up

    Pipe pipe = new Pipe("top-ten");
    pipe = new Each(pipe, new FilterJunk());

    // same sequence as the tuple that wud be generated after this function
    Fields fields = new Fields("Id", "aboutMe", "age", "creationDate", "displayName", "downVotes", "emailHash",
        "lastAccessDate", "location", "reputation", "upVotes", "views", "websiteUrl", "accountId");

    pipe = new Each(pipe, new UsersTupleGeneratorFunction(fields), Fields.RESULTS);

    // now group the fields Fields.None
    pipe = new GroupBy(pipe, Fields.NONE); // this will cause a single reducer to run -- Fields.NONE

    // now use the every pipe to use the aggregate function we wrote
    pipe = new Every(pipe, new TopNTuplesOperation(fields), Fields.ALL);

    // reduce the output fields
    // use of identity function

    Fields selectedFields = new Fields("Id", "displayName", "reputation", "upVotes", "downVotes");
    pipe = new Each(pipe, selectedFields, new Identity());

    // why not use the Retain Pipe to do the above
    pipe = new Retain(pipe, new Fields("Id", "displayName", "reputation"));

    FlowDef flowDef = new FlowDef().addSource(pipe, inTap).addTailSink(pipe, sinkTap);
    Flow<?> flow = new Hadoop2MR1FlowConnector().connect(flowDef);
    flow.complete();


  }

  @Test
  public void runCode() {
    do_as_required();
  }

}
