package net.jcomm.ex;

import com.beust.jcommander.JCommander;

public class MainClass {

  public static void main(String[] args) {

    // the normal way to parse command line arguments or use them would be
    // here the class shud be run as -- java MainClass file1.txt /root/sink/

    System.out.println("first arg:" + args[0]);
    System.out.println("second arg:" + args[1]);

    // now using jCommander
    MappedArguments mappedArguments = new MappedArguments();
    // new JCommander(mappedArguments, args); lets mock these args
    String mockArgs[] = { "-destinationSink", "/root/sink", "-sourceFile", "file1.txt" };
    // make sure of the matchers , that way we need not worry about the order of passing arguments
    new JCommander(mappedArguments, mockArgs);

    System.out.println(mappedArguments.getSource());
    System.out.println(mappedArguments.getDest());

  }

}
