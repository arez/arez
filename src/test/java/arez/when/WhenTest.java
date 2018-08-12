package arez.when;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Observable;
import arez.Observer;
import arez.Priority;
import arez.SafeFunction;
import arez.SafeProcedure;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WhenTest
  extends AbstractTest
{
  @Test
  public void when()
    throws Throwable
  {
    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final SafeFunction<Boolean> condition = () -> {
      observeDependency();
      conditionRun.incrementAndGet();
      return false;
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Component component = Arez.context().component( ValueUtil.randomString(), ValueUtil.randomString() );
    final Observer node = When.when( component, name, true, condition, procedure, Priority.NORMAL, true );

    assertEquals( node.getName(), name + ".watcher" );
    assertEquals( Arez.context().getSpy().asObserverInfo( node ).getPriority(), Priority.NORMAL );
    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void when_effectNoVerifyAction()
    throws Throwable
  {
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();

    final SafeFunction<Boolean> condition = () -> {
      observeDependency();
      return true;
    };
    When.when( Arez.context().component( ValueUtil.randomString(), ValueUtil.randomString() ),
               name,
               true,
               false,
               condition,
               effectRun::incrementAndGet,
               Priority.NORMAL,
               true );

    assertEquals( effectRun.get(), 1 );
  }

  @Test
  public void when_effectVerifyActionButNoReadsOrWrites()
    throws Throwable
  {
    ignoreObserverErrors();

    final ArezContext context = Arez.context();

    final AtomicInteger effectRun = new AtomicInteger();

    final AtomicInteger errorCount = new AtomicInteger();
    context.addObserverErrorHandler( ( observer, error, throwable ) -> {
      errorCount.incrementAndGet();
      assertNotNull( throwable );
      assertEquals( throwable.getMessage(),
                    "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
    } );

    final SafeFunction<Boolean> condition = () -> {
      observeDependency();
      return true;
    };
    When.when( context.component( ValueUtil.randomString(), ValueUtil.randomString() ),
               "X",
               true,
               true,
               condition,
               effectRun::incrementAndGet,
               Priority.NORMAL,
               true );
    assertEquals( effectRun.get(), 1 );
    assertEquals( errorCount.get(), 1 );
  }

  @Test
  public void when_minimalParameters()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Observable observable = context.observable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Observer node = When.when( condition, procedure );

    assertEquals( node.getName(), "When@1.watcher" );
    assertEquals( context.getSpy().asObserverInfo( node ).getPriority(), Priority.NORMAL );
    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }
}
