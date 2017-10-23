package org.realityforge.arez.extras;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezTestUtil;
import org.realityforge.arez.Node;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Procedure;
import org.realityforge.arez.SafeFunction;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ArezExtrasTest
  extends AbstractArezExtrasTest
{
  @Test
  public void when()
    throws Throwable
  {
    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      return false;
    };
    final Procedure procedure = effectRun::incrementAndGet;

    ArezExtras.setNextNodeId( 1 );

    final Node node = ArezExtras.when( name, true, condition, procedure );

    assertTrue( node instanceof Watcher );
    final Watcher watcher = (Watcher) node;
    assertEquals( watcher.getName(), name );
    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( Arez.context().getSpy().isReadOnly( watcher.getObserver() ), false );

    assertEquals( ArezExtras.getNextNodeId(), 1 );
  }

  @Test
  public void when_minimalParameters()
    throws Throwable
  {
    final Observable observable = Arez.context().createObservable( ValueUtil.randomString() );

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final Procedure procedure = effectRun::incrementAndGet;

    ArezExtras.setNextNodeId( 22 );
    final Node node = ArezExtras.when( condition, procedure );

    assertTrue( node instanceof Watcher );
    final Watcher watcher = (Watcher) node;
    assertEquals( watcher.getName(), "When@22" );
    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
    assertEquals( Arez.context().getSpy().isReadOnly( watcher.getObserver() ), false );

    assertEquals( ArezExtras.getNextNodeId(), 23 );
  }

  @Test
  public void toName()
  {
    // Use passed in name
    assertEquals( ArezExtras.toName( "When", "MyName" ), "MyName" );

    //synthesize name
    ArezExtras.setNextNodeId( 1 );
    assertEquals( ArezExtras.toName( "When", null ), "When@1" );
    assertEquals( ArezExtras.getNextNodeId(), 2 );

    ArezTestUtil.setEnableNames( false );

    //Ignore name
    assertEquals( ArezExtras.toName( "When", "MyName" ), null );

    //Null name also fine
    assertEquals( ArezExtras.toName( "When", null ), null );
  }
}
