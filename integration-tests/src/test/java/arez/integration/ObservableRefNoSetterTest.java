package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableRefNoSetterTest
  extends AbstractIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    private int _otherID;
    private String _other;

    @ObservableRef
    abstract arez.Observable getOtherIDObservable();

    String getOther()
    {
      getOtherIDObservable().reportObserved();
      if ( null == _other )
      {
        // Imagine that this looks up the other in a repository
        // and other is actually another ArezComponent. This is
        // the example used in replicant and HL libraries. The
        // network layer provides the ID and it is resovled locally
        _other = String.valueOf( _otherID );
      }
      return _other;
    }

    @Observable( expectSetter = false )
    int getOtherID()
    {
      return _otherID;
    }

    @Action
    void setOtherID( final int otherID )
    {
      _other = null;
      _otherID = otherID;
      getOtherIDObservable().reportChanged();
    }
  }

  @Test
  public void observableRef()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final TestComponent component = new ObservableRefNoSetterTest_Arez_TestComponent();
    context.action( () -> component.setOtherID( 1 ) );

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final AtomicInteger ttCount = new AtomicInteger();
    final AtomicInteger rtCount = new AtomicInteger();

    context.autorun( "TransportType",
                     () -> {
                       recorder.mark( "TransportType", component.getOtherID() );
                       ttCount.incrementAndGet();
                     } );
    // This is verifying that the explicit reportObserved occurs
    context.autorun( "ResolvedType",
                     () -> {
                       recorder.mark( "ResolvedType", component.getOther() );
                       rtCount.incrementAndGet();
                     } );

    assertEquals( ttCount.get(), 1 );
    assertEquals( rtCount.get(), 1 );

    // This is verifying that the explicit reportChanged occurs
    context.action( "ID Update", true, () -> component.setOtherID( 22 ) );

    assertEqualsFixture( recorder.eventsAsString() );

    assertEquals( ttCount.get(), 2 );
    assertEquals( rtCount.get(), 2 );
  }
}
