package net.emp.display;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class InsertSalaryIdentifier extends BaseOperation<Void> implements
		Function<Void> {

	public InsertSalaryIdentifier(Fields salIdentifier) {
		super(1, salIdentifier);
	}

	@Override
	public void operate(FlowProcess flowProcess, FunctionCall<Void> functionCall) {
		TupleEntry arguments = functionCall.getArguments();
		Tuple outputTuple = new Tuple();
		Integer sal = Integer.parseInt(arguments.getString(0));
		// sal values -- 1000-3500 = low, 3501- 6000 = med and >6001 hi
		if (sal <= 3500) {
			outputTuple.add("low");
		} else if (sal <= 6000) {
			outputTuple.add("med");
		} else {
			outputTuple.add("hi");
		}

		functionCall.getOutputCollector().add(outputTuple);

	}

}
