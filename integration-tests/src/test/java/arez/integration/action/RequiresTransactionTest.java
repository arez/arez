package arez.integration.action;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ObservableChangedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( { "UnusedReturnValue", "unused", "SameParameterValue" } )
public class RequiresTransactionTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class MyComponent
  {
    @Action( requireNewTransaction = false )
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
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final MyComponent component = new RequiresTransactionTest_Arez_MyComponent();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    component.myAction();

    recorder.assertEventCount( 5 );
    recorder.assertNextEvent( ActionStartedEvent.class, a -> assertEquals( a.getName(), "MyComponent.0.myAction" ) );
    recorder.assertNextEvent( TransactionStartedEvent.class,
                              a -> assertEquals( a.getName(), "MyComponent.0.myAction" ) );
    recorder.assertNextEvent( ObservableChangedEvent.class,
                              a -> assertEquals( a.getObservable().getName(), "MyComponent.0.time" ) );
    recorder.assertNextEvent( TransactionCompletedEvent.class,
                              a -> assertEquals( a.getName(), "MyComponent.0.myAction" ) );
    recorder.assertNextEvent( ActionCompletedEvent.class, a -> assertEquals( a.getName(), "MyComponent.0.myAction" ) );
  }

  @ArezComponent
  static abstract class MyComponent2
  {
    @Action( requireNewTransaction = false )
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
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final MyComponent2 component = new RequiresTransactionTest_Arez_MyComponent2();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    context.safeAction( "MyWrapperAction", true, false, component::myAction );

    recorder.assertEventCount( 7 );
    recorder.assertNextEvent( ActionStartedEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( TransactionStartedEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( ActionStartedEvent.class, a -> assertEquals( a.getName(), "MyComponent2.0.myAction" ) );
    recorder.assertNextEvent( ObservableChangedEvent.class,
                              a -> assertEquals( a.getObservable().getName(), "MyComponent2.0.time" ) );
    recorder.assertNextEvent( ActionCompletedEvent.class, a -> assertEquals( a.getName(), "MyComponent2.0.myAction" ) );
    recorder.assertNextEvent( TransactionCompletedEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( ActionCompletedEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
  }

  @ArezComponent
  static abstract class MyComponent3
  {
    @Action( requireNewTransaction = false )
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
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final MyComponent3 component = new RequiresTransactionTest_Arez_MyComponent3();

    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.safeAction( "MyWrapperAction", false, false, component::myAction ) );

    assertEquals( exception.getMessage(),
                  "Arez-0119: Attempting to create READ_WRITE transaction named 'MyComponent3.0.myAction' but it is nested in transaction named 'MyWrapperAction' with mode READ_ONLY which is not equal to READ_WRITE." );

    recorder.assertEventCount( 6 );
    recorder.assertNextEvent( ActionStartedEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( TransactionStartedEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( ActionStartedEvent.class, a -> assertEquals( a.getName(), "MyComponent3.0.myAction" ) );
    recorder.assertNextEvent( ActionCompletedEvent.class, a -> assertEquals( a.getName(), "MyComponent3.0.myAction" ) );
    recorder.assertNextEvent( TransactionCompletedEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
    recorder.assertNextEvent( ActionCompletedEvent.class, a -> assertEquals( a.getName(), "MyWrapperAction" ) );
  }
}
