package arez.integration.scheduler;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.annotations.Priority;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.ArrayList;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class PrioritizedSchedulerTest
  extends AbstractArezIntegrationTest
{
  @SuppressWarnings( "SameParameterValue" )
  @ArezComponent
  public static abstract class TestComponent
  {
    ArrayList<String> _calls = new ArrayList<>();

    @Observable
    abstract String getValue1();

    abstract void setValue1( String value );

    @Observable
    abstract String getValue2();

    abstract void setValue2( String value );

    @Observable
    abstract String getValue3();

    abstract void setValue3( String value );

    @Computed( priority = Priority.HIGH )
    String computed1()
    {
      _calls.add( "computed1" );
      return getValue1();
    }

    @Computed( priority = Priority.NORMAL )
    String computed2()
    {
      _calls.add( "computed2" );
      return getValue2();
    }

    @Computed( priority = Priority.LOW )
    String computed3()
    {
      _calls.add( "computed3" );
      return getValue3();
    }

    @Observed( priority = Priority.HIGH )
    void autorun1a()
    {
      _calls.add( "autorun1a" );
      getValue1();
    }

    @Observed( priority = Priority.HIGH )
    void autorun1b()
    {
      _calls.add( "autorun1b" );
      computed1();
    }

    @Observed( priority = Priority.NORMAL )
    void autorun2a()
    {
      _calls.add( "autorun2a" );
      getValue1();
      getValue2();
    }

    @Observed( priority = Priority.NORMAL )
    void autorun2b()
    {
      _calls.add( "autorun2b" );
      computed1();
    }

    @Observed( priority = Priority.LOW )
    void autorun3a()
    {
      _calls.add( "autorun3a" );
      getValue1();
      getValue2();
      getValue3();
    }

    @Observed( priority = Priority.LOW )
    void autorun3b()
    {
      _calls.add( "autorun3b" );
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
                  "[autorun1a, autorun1b, computed1, autorun2a, autorun2b, autorun3a, autorun3b]" );

    component._calls.clear();

    safeAction( () -> component.setValue1( "1" ) );

    assertEquals( component._calls.size(), 7 );
    assertEquals( component._calls.toString(),
                  "[autorun1a, computed1, autorun1b, autorun2a, autorun2b, autorun3a, autorun3b]" );

    component._calls.clear();

    safeAction( () -> component.setValue2( "2" ) );

    assertEquals( component._calls.size(), 2 );
    assertEquals( component._calls.toString(), "[autorun2a, autorun3a]" );

    component._calls.clear();

    safeAction( () -> component.setValue3( "3" ) );

    assertEquals( component._calls.size(), 1 );
    assertEquals( component._calls.toString(), "[autorun3a]" );

    Disposable.dispose( component );

    assertMatchesFixture( recorder );
  }
}
