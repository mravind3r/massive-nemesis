package net.stackoverflow.patterns.numerics.sampling;

import java.util.Random;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;

public class RandomOperation extends BaseOperation<Object> implements Filter<Object> {

  private double percentage = 0.50; // just need 30% of the records
  private Random random = new Random();
  @Override
  public boolean isRemove(FlowProcess flowProcess, FilterCall<Object> filterCall) {
    return random.nextDouble() < percentage;
  }



}
