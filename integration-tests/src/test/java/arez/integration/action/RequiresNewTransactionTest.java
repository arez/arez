package arez.integration.action;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( { "UnusedReturnValue", "unused", "SameParameterValue" } )
public final class RequiresNewTransactionTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class MyComponent
  {
    @Action( requireNewTransaction = true )
    void myAction()
    {
      getTime();
      setTime( 33 );
    }

    @Observable
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void topLevelAction()
  {
    final ArezContext context = Arez.context();

    final MyComponent component = new RequiresNewTransactionTest_Arez_MyComponent();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    component.myAction();

    recorder.assertEventCount( 5 );
    recorder.assertNextEvent( ActionStartEvent.class, a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent.1.myAction" ) );
    recorder.assertNextEvent( TransactionStartEvent.class,
                              a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent.1.myAction" ) );
    recorder.assertNextEvent( ObservableValueChangeEvent.class,
                              a -> assertEquals( a.getObservableValue().getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent.1.time" ) );
    recorder.assertNextEvent( TransactionCompleteEvent.class,
                              a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent.1.myAction" ) );
    recorder.assertNextEvent( ActionCompleteEvent.class, a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent.1.myAction" ) );
  }

  @ArezComponent
  static abstract class MyComponent2
  {
    @Action( requireNewTransaction = true )
    void myAction()
    {
      getTime();
      setTime( 33 );
    }

    @Observable
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void nestedAction()
  {
    final ArezContext context = Arez.context();

    final MyComponent2 component = new RequiresNewTransactionTest_Arez_MyComponent2();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    context.safeAction( "MyWrapperAction", component::myAction, ActionFlags.NO_VERIFY_ACTION_REQUIRED );

    recorder.assertEventCount( 9 );
    recorder.assertNextEvent( ActionStartEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( TransactionStartEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( ActionStartEvent.class, a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent2.1.myAction" ) );
    recorder.assertNextEvent( TransactionStartEvent.class,
                              a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent2.1.myAction" ) );
    recorder.assertNextEvent( ObservableValueChangeEvent.class,
                              a -> assertEquals( a.getObservableValue().getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent2.1.time" ) );
    recorder.assertNextEvent( TransactionCompleteEvent.class,
                              a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent2.1.myAction" ) );
    recorder.assertNextEvent( ActionCompleteEvent.class, a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent2.1.myAction" ) );
    recorder.assertNextEvent( TransactionCompleteEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( ActionCompleteEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
  }

  @ArezComponent
  static abstract class MyComponent3
  {
    @Action( requireNewTransaction = true )
    void myAction()
    {
      getTime();
      setTime( 33 );
    }

    @Observable
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void nestedAction_in_ReadOnlyAction()
  {
    final ArezContext context = Arez.context();

    final MyComponent3 component = new RequiresNewTransactionTest_Arez_MyComponent3();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    assertInvariant( () -> context.safeAction( "MyWrapperAction", component::myAction, ActionFlags.READ_ONLY ),
                     "Arez-0119: Attempting to create READ_WRITE transaction named 'arez_integration_action_RequiresNewTransactionTest_MyComponent3.1.myAction' but it is nested in transaction named 'MyWrapperAction' with mode READ_ONLY which is not equal to READ_WRITE." );

    recorder.assertEventCount( 6 );
    recorder.assertNextEvent( ActionStartEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( TransactionStartEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( ActionStartEvent.class, a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent3.1.myAction" ) );
    recorder.assertNextEvent( ActionCompleteEvent.class, a -> assertEquals( a.getName(), "arez_integration_action_RequiresNewTransactionTest_MyComponent3.1.myAction" ) );
    recorder.assertNextEvent( TransactionCompleteEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( ActionCompleteEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
  }
}
