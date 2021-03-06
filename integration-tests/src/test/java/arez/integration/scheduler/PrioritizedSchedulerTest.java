package arez.integration.scheduler;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.Priority;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.ArrayList;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class PrioritizedSchedulerTest
  extends AbstractArezIntegrationTest
{
  @SuppressWarnings( "SameParameterValue" )
  @ArezComponent
  static abstract class TestComponent
  {
    final ArrayList<String> _calls = new ArrayList<>();

    @Observable
    abstract String getValue1();

    abstract void setValue1( String value );

    @Observable
    abstract String getValue2();

    abstract void setValue2( String value );

    @Observable
    abstract String getValue3();

    abstract void setValue3( String value );

    @Memoize( priority = Priority.HIGH )
    String computed1()
    {
      _calls.add( "computed1" );
      return getValue1();
    }

    @Memoize( priority = Priority.NORMAL )
    String computed2()
    {
      _calls.add( "computed2" );
      return getValue2();
    }

    @Memoize( priority = Priority.LOW )
    String computed3()
    {
      _calls.add( "computed3" );
      return getValue3();
    }

    @Observe( priority = Priority.HIGH )
    void observer1a()
    {
      _calls.add( "observer1a" );
      getValue1();
    }

    @Observe( priority = Priority.HIGH )
    void observer1b()
    {
      _calls.add( "observer1b" );
      computed1();
    }

    @Observe( priority = Priority.NORMAL )
    void observer2a()
    {
      _calls.add( "observer2a" );
      getValue1();
      getValue2();
    }

    @Observe( priority = Priority.NORMAL )
    void observer2b()
    {
      _calls.add( "observer2b" );
      computed1();
    }

    @Observe( priority = Priority.LOW )
    void observer3a()
    {
      _calls.add( "observer3a" );
      getValue1();
      getValue2();
      getValue3();
    }

    @Observe( priority = Priority.LOW )
    void observer3b()
    {
      _calls.add( "observer3b" );
      computed1();
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final TestComponent component = new PrioritizedSchedulerTest_Arez_TestComponent();

    assertEquals( component._calls.size(), 7 );
    assertEquals( component._calls.toString(),
                  "[observer1a, observer1b, computed1, observer2a, observer2b, observer3a, observer3b]" );

    component._calls.clear();

    safeAction( () -> component.setValue1( "1" ) );

    assertEquals( component._calls.size(), 7 );
    assertEquals( component._calls.toString(),
                  "[observer1a, computed1, observer1b, observer2a, observer2b, observer3a, observer3b]" );

    component._calls.clear();

    safeAction( () -> component.setValue2( "2" ) );

    assertEquals( component._calls.size(), 2 );
    assertEquals( component._calls.toString(), "[observer2a, observer3a]" );

    component._calls.clear();

    safeAction( () -> component.setValue3( "3" ) );

    assertEquals( component._calls.size(), 1 );
    assertEquals( component._calls.toString(), "[observer3a]" );

    Disposable.dispose( component );

    assertMatchesFixture( recorder );
  }
}
