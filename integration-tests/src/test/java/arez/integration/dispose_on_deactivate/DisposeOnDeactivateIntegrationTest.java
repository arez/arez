package arez.integration.dispose_on_deactivate;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.component.ComponentObservable;
import arez.integration.AbstractIntegrationTest;
import arez.integration.SpyEventRecorder;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposeOnDeactivateIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final Model1 model = Model1.create();

    assertEquals( Disposable.isDisposed( model ), false );

    final Observer observer = context.autorun( () -> ComponentObservable.observe( model ) );

    assertEquals( Disposable.isDisposed( observer ), false );

    Disposable.dispose( observer );

    assertEquals( Disposable.isDisposed( model ), true );
    assertEquals( Disposable.isDisposed( observer ), true );

    assertEqualsFixture( recorder.eventsAsString() );
  }

  @ArezComponent( disposeOnDeactivate = true, allowEmpty = true )
  static abstract class Model1
  {
    static Model1 create()
    {
      return new DisposeOnDeactivateIntegrationTest_Arez_Model1();
    }
  }
}
