package arez.integration.dispose_on_deactivate;

import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposeOnDeactivateIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final Model1 model = Model1.create();

    assertFalse( Disposable.isDisposed( model ) );

    final Observer observer = observer( () -> ComponentObservable.observe( model ) );

    assertFalse( Disposable.isDisposed( observer ) );

    Disposable.dispose( observer );

    assertTrue( Disposable.isDisposed( model ) );
    assertTrue( Disposable.isDisposed( observer ) );

    assertMatchesFixture( recorder );
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
