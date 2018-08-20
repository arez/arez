package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableRefNoSetterTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    private int _otherID;
    private String _other;

    @ObservableRef
    abstract ObservableValue getOtherIDObservable();

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
    component.setOtherID( 1 );

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final AtomicInteger ttCount = new AtomicInteger();
    final AtomicInteger rtCount = new AtomicInteger();

    context.autorun( "TransportType",
                     () -> {
                       observeADependency();
                       recorder.mark( "TransportType", component.getOtherID() );
                       ttCount.incrementAndGet();
                     } );
    // This is verifying that the explicit reportObserved occurs
    context.autorun( "ResolvedType",
                     () -> {
                       observeADependency();
                       recorder.mark( "ResolvedType", component.getOther() );
                       rtCount.incrementAndGet();
                     } );

    assertEquals( ttCount.get(), 1 );
    assertEquals( rtCount.get(), 1 );

    // This is verifying that the explicit reportChanged occurs
    component.setOtherID( 22 );

    assertMatchesFixture( recorder );

    assertEquals( ttCount.get(), 2 );
    assertEquals( rtCount.get(), 2 );
  }
}
