package net.emp.display;

import java.util.Iterator;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Buffer;
import cascading.operation.BufferCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class SalaryIdSetter extends BaseOperation<Void> implements Buffer<Void> {

	private static final long serialVersionUID = 1L;

	public SalaryIdSetter(Fields field) {
		super(1, field);
	}

	@Override
	public void operate(FlowProcess flowProcess, BufferCall<Void> bufferCall) {
		Iterator<TupleEntry> argumentsIterator = bufferCall
				.getArgumentsIterator();
		boolean maxVal = false;
		while (argumentsIterator.hasNext()) {
			TupleEntry t = argumentsIterator.next();
			maxVal = t.getTuple().getBoolean(0);
			if (maxVal)
				break;
		}

		while (argumentsIterator.hasNext()) {
			Tuple t = new Tuple();
			t.add(maxVal);
			bufferCall.getOutputCollector().add(t);
		}
	}
}
