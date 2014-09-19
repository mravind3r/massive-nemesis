package net.stackoverflow.util;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.stackoverflow.patterns.numerics.counters.ReputationCounter;
import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.operation.OperationCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;



public class UsersTupleGeneratorFunction extends BaseOperation<Object> implements Function<Object> {

  // sample xml fragment that needs to be converted to Users domain class

  // <row Id="-1" Reputation="1" CreationDate="2010-09-01T17:32:37.120" DisplayName="Community"
  // LastAccessDate="2010-09-01T17:32:37.120" Location="on the server farm"
  // AboutMe="&lt;p&gt;Hi, I'm not really a person.&lt;/p&gt;&#xD;&#xA;&lt;p&gt;I'm a background process that helps keep this site clean!&lt;/p&gt;&#xD;&#xA;&lt;p&gt;I do things like&lt;/p&gt;&#xD;&#xA;&lt;ul&gt;&#xD;&#xA;&lt;li&gt;Randomly poke old unanswered questions every hour so they get some attention&lt;/li&gt;&#xD;&#xA;&lt;li&gt;Own community questions and answers so nobody gets unnecessary reputation from them&lt;/li&gt;&#xD;&#xA;&lt;li&gt;Own downvotes on spam/evil posts that get permanently deleted&lt;/li&gt;&#xD;&#xA;&lt;li&gt;Own suggested edits from anonymous users&lt;/li&gt;&#xD;&#xA;&lt;/ul&gt;"
  // Views="0" UpVotes="117930" DownVotes="7972" EmailHash="a007be5a61f6aa8f3e85ae2fc18dd66e" />

  // nb some fields may be missing in the above fragment but are present in other fragments
  

  private static final long serialVersionUID = 1L;

  private Unmarshaller jaxbUnmarshaller;

  // @input fields -- these are the fields that I need as a output of this peration
  // fields will be based on Users.xml data
  public UsersTupleGeneratorFunction(Fields fields) {
    super(1, fields);
    // here 1 indicates that I am going to process 1 field
    // in this case it will be Fields("line") default
  }

  @Override
  public void prepare(FlowProcess flowProcess, OperationCall<Object> operationCall) {
    // TODO Auto-generated method stub
    super.prepare(flowProcess, operationCall);
    JAXBContext jaxbContext;
    try {
      jaxbContext = JAXBContext.newInstance(Users.class);
      jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    } catch (JAXBException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void operate(FlowProcess flowProcess, FunctionCall<Object> functionCall) {
    TupleEntry tupleEntry = functionCall.getArguments(); // get the data from here
    Users users = null;
    try {
      users = (Users) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(tupleEntry
          .getString(new Fields("line"))
          .getBytes()));
    } catch (JAXBException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // got to output the tuples now
    // the way the tuple has been contructed the fields shud be passed in the same sequence in the contructor
    Tuple tuple = new Tuple();
    tuple.add(users.getId()); // users -- good faith that its initialized
    tuple.add(users.getAboutMe());
    tuple.add(users.getAge());
    tuple.add(users.getCreationDate());
    tuple.add(users.getDisplayName());
    tuple.add(users.getDownVotes());
    tuple.add(users.getEmailHash());
    tuple.add(users.getLastAccessDate());
    tuple.add(users.getLocation());
    tuple.add(users.getReputation());
    tuple.add(users.getUpVotes());
    tuple.add(users.getViews());
    tuple.add(users.getWebsiteUrl());
    tuple.add(users.getAccountId());

    // the hack to get the counters going

    if (users.getReputation() <= 100) {
      flowProcess.increment(ReputationCounter.COUNTER_1, 1);
    } else if (users.getReputation() <= 500) {
      flowProcess.increment(ReputationCounter.COUNTER_2, 1);
    } else {
      flowProcess.increment(ReputationCounter.COUNTER_3, 1);
    }

    functionCall.getOutputCollector().add(tuple);

  }

}
