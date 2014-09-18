package net.stackoverflow.patterns.numerics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cascading.flow.FlowProcess;
import cascading.operation.Aggregator;
import cascading.operation.AggregatorCall;
import cascading.operation.BaseOperation;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class FirstLastDateNumCommentsTupleCreator extends BaseOperation<FirstLastDateNumCommentsTupleCreator.Context>
  implements Aggregator<FirstLastDateNumCommentsTupleCreator.Context>{

  private SimpleDateFormat sdf;
	
	static class Context {
		Date firstComment ;
		Date lastComment;
		int totalCommentCount;
		int userId;
	}
	
	// push in 3 fields that will form the outgoing tuple
	// and the tuple we shall be actually be operating upon will have 1 fields date
	
	FirstLastDateNumCommentsTupleCreator(Fields outGoingTuple){
		super(2,outGoingTuple); // 1 indicate the tuple fileds being operated upon
	}
	

	@Override
	public void start(FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
    // here we set a new context object for every group-- userid grouping
		aggregatorCall.setContext(new Context());
    sdf = new SimpleDateFormat("yyyy-MM-dd"); // initialize this in start
	}

	@Override
	public void aggregate(FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
		// the logic goes here , this runs for every group
		
		TupleEntry tupleEntry = aggregatorCall.getArguments();
		String creationDate = tupleEntry.getString(new Fields("CreationDate"));
		Integer userId = tupleEntry.getInteger(new Fields("UserId"));
		
		// trim string to just get date
		String d1 = creationDate.substring(0,10);
		

		Context context = aggregatorCall.getContext();
		// logic is to check this date against the one set inside the context object
		Date maxDate = context.lastComment;
		Date mindate = context.firstComment;
		int counter = context.totalCommentCount;
		if(maxDate==null || mindate==null){ // set this with first value as arbitarty
			try {
				maxDate = sdf.parse(d1);
				mindate = sdf.parse(d1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{ 
			 Date tupleDate;
			try {
				tupleDate = sdf.parse(d1);
				maxDate = tupleDate.after(maxDate)?tupleDate:maxDate;
				mindate = tupleDate.before(mindate)?tupleDate:mindate;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		
		context.userId = userId;
		context.firstComment = mindate;
		context.lastComment = maxDate;
		context.totalCommentCount = ++counter;
		
		// reset the context
		
		aggregatorCall.setContext(context);
		
		
	}

	@Override
	public void complete(FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
		
		Context context = aggregatorCall.getContext();
		Tuple tuple = new Tuple();
		tuple.add(context.firstComment);
		tuple.add(context.lastComment);
		tuple.add(context.totalCommentCount);
		aggregatorCall.getOutputCollector().add(tuple);
		
	}
	

}
