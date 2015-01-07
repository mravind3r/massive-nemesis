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

/**
 * can do better with the mappedArguments.getDest() -- this return a string , wud it not be great if it reutrns a hadoop
 * Path object ?? so in the MappedSrguments class
 * 
 * @Parameter(names = HIT_CLEANSE_PATH, description = "Hit Cleanse data", converter = PathArgumentConverter.class,
 *                  required = true) private Path hitCleansed;
 */

/**
 * import org.apache.hadoop.fs.Path; import com.beust.jcommander.IStringConverter; public class PathArgumentConverter
 * implements IStringConverter<Path> { * @Override public Path convert(String pathName) { return new Path(pathName); } }
 */

