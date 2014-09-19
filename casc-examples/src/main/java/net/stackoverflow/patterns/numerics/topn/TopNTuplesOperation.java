package net.stackoverflow.patterns.numerics.topn;

import java.util.Set;
import java.util.TreeMap;

import cascading.flow.FlowProcess;
import cascading.operation.Aggregator;
import cascading.operation.AggregatorCall;
import cascading.operation.BaseOperation;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class TopNTuplesOperation extends BaseOperation<TopNTuplesOperation.Context> implements
    Aggregator<TopNTuplesOperation.Context> {

  public TopNTuplesOperation(Fields fields) {
    super(fields); // no fields specified as i want all fields that comeinto the tuple entry to be outputted
  }

  static class Context {
    private TreeMap<Integer, Tuple> top10 = new TreeMap<Integer, Tuple>();
  }

  private static final long serialVersionUID = 1L;





  @Override
  public void start(FlowProcess flowProcess, AggregatorCall<Context> aggregatorCall) {
    aggregatorCall.setContext(new Context());
  }

  @Override
  public void aggregate(FlowProcess flowProcess, AggregatorCall<Context> aggregatorCall) {
    // iterate over the values to find the top 10 values
    // keep in mind that the groupby was done via Fields.None in the previous pipe
    // we are not grouping on any specific value, we are treating the entire dataset as one group

    TupleEntry tupleEntry = aggregatorCall.getArguments();
    Integer reputation = tupleEntry.getInteger(new Fields("reputation"));
    if (reputation != null) {

      // very very important to do add new Tuple(tupe)
      // this inside the mapper is collected and passed over to the reducers
      // just adding the refrence will not give the right results
      TreeMap<Integer, Tuple> ref = aggregatorCall.getContext().top10;
      ref.put(reputation, new Tuple(tupleEntry.getTuple()));
      // check the mapsize and remove the tail
      if (ref.size() > 10) {
        ref.remove(ref.firstKey());
       }
    }

  }


  @Override
  public void complete(FlowProcess flowProcess, AggregatorCall<Context> aggregatorCall) {
    Set<Integer> keys = aggregatorCall.getContext().top10.keySet();
    for (Integer key : keys) {
      aggregatorCall.getOutputCollector().add(aggregatorCall.getContext().top10.get(key));
    }

  }

}
