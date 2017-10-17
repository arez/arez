package org.realityforge.arez.integration;

import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.ObservableRef;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableRefTest
  extends AbstractIntegrationTest
{
  @ArezComponent
  static class TestComponent
  {
    private int _otherID;
    private String _other;

    @ObservableRef
    org.realityforge.arez.Observable getOtherIDObservable()
    {
      throw new IllegalStateException();
    }

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

    @Observable
    int getOtherID()
    {
      return _otherID;
    }

    void setOtherID( final int otherID )
    {
      _other = null;
      _otherID = otherID;
    }
  }

  @Test
  public void observableRef()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final TestComponent component = new ObservableRefTest$Arez_TestComponent();
    context.action( () -> component.setOtherID( 1 ) );

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final AtomicInteger ttCount = new AtomicInteger();
    final AtomicInteger rtCount = new AtomicInteger();

    context.autorun( "TransportType",
                     () -> {
                       record( recorder, "TransportType", component.getOtherID() );
                       ttCount.incrementAndGet();
                     } );
    // This is verifying that the explict reportObserved occurs
    context.autorun( "ResolvedType",
                     () -> {
                       record( recorder, "ResolvedType", component.getOther() );
                       rtCount.incrementAndGet();
                     } );

    assertEquals( ttCount.get(), 1 );
    assertEquals( rtCount.get(), 1 );

    context.action( "ID Update", true, () -> component.setOtherID( 22 ) );

    assertEqualsFixture( recorder.eventsAsString() );

    assertEquals( ttCount.get(), 2 );
    assertEquals( rtCount.get(), 2 );
  }
}
