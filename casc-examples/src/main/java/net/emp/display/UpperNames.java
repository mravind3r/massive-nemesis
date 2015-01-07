package net.emp.display;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class UpperNames extends BaseOperation<Void> implements Function<Void> {

	// i know only one field will come through ie ename cos thats the only one i
	// need

	public UpperNames(Fields name) {
		super(1, name);
	}

	@Override
	public void operate(FlowProcess flowProcess, FunctionCall<Void> functionCall) {
		TupleEntry arguments = functionCall.getArguments();
		Tuple outputTuple = new Tuple();
		outputTuple.add(arguments.getString(0).toUpperCase());
		functionCall.getOutputCollector().add(outputTuple);
	}

}
