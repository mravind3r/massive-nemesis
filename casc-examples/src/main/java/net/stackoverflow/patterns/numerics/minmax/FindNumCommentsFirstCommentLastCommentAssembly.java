package net.stackoverflow.patterns.numerics.minmax;

import net.stackoverflow.util.CommentsTupleGeneratorFunction;
import net.stackoverflow.util.FilterJunk;

import org.junit.Test;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.pipe.Each;
import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class FindNumCommentsFirstCommentLastCommentAssembly {

  // here will write an assembly that will give me an output as follows
  // given the comments xml file , this program will generate a new dataset that will have
  // first date the user commented, last date user commented and the total comments.

  public void give_me_an_output_as_defined() {
    final String dir = System.getProperty("user.dir");
    String inputSource = dir + "/src/main/resources/net/do-not-open-in-eclipse-large-files/Comments.xml";

    // check the results here after the code is executed
    String sinkFolder = dir + "/src/main/resources/net/results";

    Scheme inputScheme = new TextLine(new Fields("line")); // "line" ??? this is default given by cascading api
    Tap inTap = new Hfs(inputScheme, inputSource);
    Tap sink = new Hfs(new TextLine(), sinkFolder, SinkMode.REPLACE);

    Pipe pipe = new Pipe("copy-pipe");
    pipe = new Each(pipe, new FilterJunk()); // remove xml processing text

    // I want the following fields and values attached to them, based on the comments.xml file
    Fields fields = new Fields("Id", "PostId", "Score", "Text", "CreationDate", "UserId");

    // @input fields -- thats how thw output tuples shud be like
    pipe = new Each(pipe, new CommentsTupleGeneratorFunction(fields), Fields.RESULTS); // has got jaxb parser that will parse
                                                                               // the xml and seperate content
    // pipe = new Each(pipe, new Debug());
    // can use it to spit out whats happening internally on the console uncomment as needed

    // the problem statement says find total count and first and last commented dates
    // thus grouping on the userid first
    pipe = new GroupBy(pipe, new Fields("UserId"));

    // need a custom aggregator to find the first and last time user commented
    Fields outputFields = new Fields("min", "max", "total"); // the output that I am interested in , rest of fields
                                                            // ignore

    // @input inputFields -- result in these fields only
    // custom aggregator to do the actual business logic
    pipe = new Every(pipe, new FirstLastDateNumCommentsTupleCreator(outputFields));

    // if i want to select the incoming fields
    // Fields inputFields = new Fields(.....);
    // pipe = new Every(pipe,inputFields, new FirstLastDateNumCommentsTupleCreator(inputFields));

    FlowDef flowDef = new FlowDef().addSource(pipe, inTap).addTailSink(pipe, sink);
    Flow<?> flow = new HadoopFlowConnector().connect(flowDef);
    flow.complete();

  }


  // use junit to run this
  @Test
  public void runCode() {
    give_me_an_output_as_defined();
  }

}
