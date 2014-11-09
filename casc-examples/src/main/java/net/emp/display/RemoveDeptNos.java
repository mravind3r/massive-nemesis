package net.emp.display;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;
import cascading.tuple.TupleEntry;

import com.google.common.base.Predicate;

public class RemoveDeptNos extends BaseOperation<Void> implements Filter<Void> {
	private static final long serialVersionUID = 1L;
	private static Predicate<String> filter;

	public RemoveDeptNos(Predicate<String> filter) {
		super(1);
		this.filter = filter;
	}

	@Override
	public boolean isRemove(FlowProcess flowProcess, FilterCall<Void> filterCall) {
		TupleEntry tupleEntry = filterCall.getArguments();
		String content = tupleEntry.getTuple().getString(0);
		return filter.apply(content);
	}
}
