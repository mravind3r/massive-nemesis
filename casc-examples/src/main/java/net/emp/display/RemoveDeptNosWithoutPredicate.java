package net.emp.display;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class RemoveDeptNosWithoutPredicate extends BaseOperation<Void>
		implements Filter<Void> {

	// private static Predicate<String> filter;
	// private static final long serialVersionUID = 1L;
	//
	// public RemoveDeptNosWithoutPredicate(Predicate<String> filter) {
	// super(1);
	// this.filter = filter;
	// }

	@Override
	public boolean isRemove(FlowProcess flowProcess, FilterCall<Void> filterCall) {
		TupleEntry tupleEntry = filterCall.getArguments();
		Tuple t = tupleEntry.getTuple();
		System.out.println(t.getString(0));
		return false;
	}

}
