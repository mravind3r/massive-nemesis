package net.stackoverflow.util;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

// this operation will generate the tuples 
public class TupleGeneratorFunction extends BaseOperation<Object> implements Function<Object> {

  private Unmarshaller jaxbUnmarshaller;

  public TupleGeneratorFunction(Fields fields) {
    super(1, fields);
    // 1 = new Fields("line") as this will receive the results of the previous pipe
    // fields = outgoing tuple
  }

  @Override
  public void prepare(FlowProcess flowProcess, OperationCall<Object> operationCall) {
    // TODO Auto-generated method stub
    super.prepare(flowProcess, operationCall);
    JAXBContext jaxbContext;
    try {
      jaxbContext = JAXBContext.newInstance(Comment.class);
      jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    } catch (JAXBException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public void operate(FlowProcess flowProcess, FunctionCall<Object> functionCall) {
    TupleEntry arguments = functionCall.getArguments();
    Comment comments = null;
    try {
      comments = (Comment) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(arguments
          .getString(new Fields("line"))
          .getBytes()));
    } catch (JAXBException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Tuple tuple = new Tuple();
    tuple.add(comments.getId());
    tuple.add(comments.getPostId());
    tuple.add(comments.getScore());
    tuple.add(comments.getText());
    tuple.add(comments.getCreationDate());
    tuple.add(comments.getUserId());

    functionCall.getOutputCollector().add(tuple);

    // just add tuple ,
    // as the fileds sections is already defined in the constructor

  }

}
