package net.stackoverflow.patterns.insertextrafields;

import org.junit.Test;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;
import cascading.operation.Debug;
import cascading.operation.Insert;
import cascading.operation.regex.RegexSplitter;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.pipe.assembly.Retain;
import cascading.pipe.assembly.Unique;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class InsertBooleanFlag {

  public void insert_flag_based_on_calculation() {
    String home = System.getProperty("user.dir");

    String input = home + "/src/main/resources/net/do-not-open-in-eclipse-large-files/NYSE_daily";
    String outputDir = home + "/src/main/resources/net/results";

    // NYSE CLI 2009-12-31 35.39 35.70 34.50 34.57 890100 34.12 -- sample data

    Scheme inScheme = new TextLine();

    Tap inTap = new Hfs(inScheme, input);
    Tap sink = new Hfs(inScheme, outputDir, SinkMode.REPLACE);

    RegexSplitter splitter = new RegexSplitter(new Fields("exchange", "script", "date", "hi", "lo", "start", "close",
        "vol", "misc"), "\t");

    // the below cause the output tuple as
    // ['NYSE', 'CVA 2009-01-02 21.76 22.80 21.46 21.71 1344600 21.71']
    // RegexSplitter splitter = new RegexSplitter(new Fields("exchange", "script"), "\t");

    Pipe pipe = new Pipe("unique-pipe");

    // let the entire line pass thru , we split on the regexsplitter function that captures only 2 values
    pipe = new Each(pipe, new Fields("line"), splitter);
    pipe = new Unique(pipe, new Fields("script"));
    pipe = new Retain(pipe, new Fields("script"));

    // whats the use?? better write a function and based onthe logic insert value
    // re-create the tuple and spit it out in the argCollector()
    pipe = new Each(pipe, new Fields("script"), new Insert(new Fields("flag"), true), Fields.ALL);
    pipe = new Each(pipe, new Debug());

    FlowDef flowDef = new FlowDef();
    flowDef.addSource(pipe, inTap).addTailSink(pipe, sink);
    Flow<?> flow = new Hadoop2MR1FlowConnector().connect(flowDef);
    flow.complete();

  }

  @Test
  public void runCode() {
    insert_flag_based_on_calculation();
  }

}
