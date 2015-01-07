package net.emp.display;

import net.emp.Constants;

import org.apache.commons.lang.StringUtils;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.operation.Debug;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class ReadEmpData {

	public static void main(String[] args) {
		String home = System.getProperty("user.dir");

		String sourceFile = home + Constants.empSrcFile;
		String sinkFolder = home + Constants.outputFolder;

		Scheme inScheme = new TextDelimited(new Fields("empno", "name", "sal",
				"deptno"), true, ",");

		Tap inTap = new Hfs(inScheme, sourceFile);
		Tap outputTap = new Hfs(new TextDelimited(), sinkFolder,
				SinkMode.REPLACE);

		Pipe pipe = new Pipe("read-emp-data-caps");

		// filter records if the dept number is 5

		Predicate<String> deptFilter = new Predicate<String>() {

			@Override
			public boolean apply(String arg0) {
				Integer deptNo = Integer.parseInt(arg0.toString());
				return deptNo == 5;
			}
		};

		pipe = new Each(pipe, new Fields("deptno"), new RemoveDeptNos(
				deptFilter));

		// uppercase names

		Function<String, String> upper = new Function<String, String>() {

			@Override
			public String apply(String arg0) {
				return StringUtils.upperCase(arg0);
			}
		};

		pipe = new Each(pipe, new Fields("name"), new UpperNames(new Fields(
				"name")), Fields.REPLACE);

		// now insert field based on sal values -- 1000-3500 = low, 3501- 6000 =
		// med >6001 hi
		pipe = new Each(pipe, new Fields("sal"), new InsertSalaryIdentifier(
				new Fields("salIdentifier")), Fields.ALL);
		pipe = new Each(pipe, new Debug());

		// now use the aggregator
		// group by deptno , then check for hi-low-med , if anyone is hi make
		// all of them high in salIdentifier

		FlowDef flowDef = new FlowDef();
		flowDef.addSource(pipe, inTap).addTailSink(pipe, outputTap);
		Flow<?> flow = new HadoopFlowConnector().connect(flowDef);
		flow.complete();
	}
}
