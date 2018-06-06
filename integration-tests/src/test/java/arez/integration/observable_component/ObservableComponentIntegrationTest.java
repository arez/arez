package arez.integration.observable_component;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.component.ComponentObservable;
import arez.integration.AbstractIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableComponentIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void disposeNotifiesComponentObservable()
    throws Throwable
  {
    final Model model = Model.create();
    final AtomicInteger callCount = new AtomicInteger();
    Arez.context().autorun( () -> {
      ComponentObservable.observe( model );
      callCount.incrementAndGet();
    } );
    assertEquals( callCount.get(), 1 );

    Disposable.dispose( model );

    assertEquals( callCount.get(), 2 );
  }

  @Test
  public void disposeNoNotifiesWhenNotComponentObservable()
    throws Throwable
  {
    final Model2 model = Model2.create();
    final AtomicInteger callCount = new AtomicInteger();
    Arez.context().autorun( () -> {
      ComponentObservable.observe( model );
      callCount.incrementAndGet();
    } );
    assertEquals( callCount.get(), 1 );

    Disposable.dispose( model );

    assertEquals( callCount.get(), 1 );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model
  {
    @Nonnull
    static Model create()
    {
      return new ObservableComponentIntegrationTest_Arez_Model();
    }
  }

  @ArezComponent( allowEmpty = true, observable = false )
  static abstract class Model2
  {
    @Nonnull
    static Model2 create()
    {
      return new ObservableComponentIntegrationTest_Arez_Model2();
    }
  }
}
