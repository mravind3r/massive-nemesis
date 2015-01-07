package net.jcomm.ex;

import com.beust.jcommander.Parameter;

public class MappedArguments {

  // here i create 2 fields that will be ampped

  // -sourceFile should match the arguments list in the main class
  @Parameter(names = "-sourceFile", description = "source file to be parsed", required = true)
  private String source;

  public String getSource() {
    return source;
  }

  public String getDest() {
    return dest;
  }

  @Parameter(names = "-destinationSink", description = "destination folder where results of mapreduce will be dumpled", required = true)
  private String dest;

}
