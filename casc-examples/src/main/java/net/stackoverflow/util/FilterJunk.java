package net.stackoverflow.util;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Filter;
import cascading.operation.FilterCall;
import cascading.tuple.Fields;
import cascading.tuple.TupleEntry;

public class FilterJunk extends BaseOperation<Object> implements Filter<Object>{

  /**
   * This operation will get rid of the lines of text in the xml file that I do not want to process. First I strip this
   * file of unwanted text snippets like the above to get a dataset will is purely consisting of xml fragments seperated
   * by \n default delimiter.
   */

  // eg <?xml version="1.0" encoding="utf-8"?> is not needed
  // the output after applying this operation to the Each pipe will result in just well formed xml fragments

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isRemove(FlowProcess flowProcess,
			FilterCall<Object> filterCall) {
		TupleEntry tupleEntry = filterCall.getArguments();
		String content = tupleEntry.getString(new Fields("line"));
		return ( content.equalsIgnoreCase("</comments>")
				|| content.equalsIgnoreCase("<comments>")
				|| content.contains("<?xml"));
		
	}

}
