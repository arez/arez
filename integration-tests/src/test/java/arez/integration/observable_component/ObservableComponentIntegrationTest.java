package arez.integration.observable_component;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableComponentIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void disposeNotifiesComponentObservable()
    throws Throwable
  {
    final Model model = Model.create();
    final AtomicInteger callCount = new AtomicInteger();
    Arez.context().autorun( () -> {
      observeADependency();
      ComponentObservable.observe( model );
      callCount.incrementAndGet();
    } );
    assertEquals( callCount.get(), 1 );

    Disposable.dispose( model );

    assertEquals( callCount.get(), 2 );
  }

  @ArezComponent( allowEmpty = true, observable = Feature.ENABLE )
  static abstract class Model
  {
    @Nonnull
    static Model create()
    {
      return new ObservableComponentIntegrationTest_Arez_Model();
    }
  }
}
