package arez.integration.dispose;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.PreDispose;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class AllowWriteInDisposeIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final String name = ValueUtil.randomString();
    final Model1 model1 = Model1.create( name );
    final Model2 model2 = Model2.create( model1 );

    final AtomicInteger callCount = new AtomicInteger();
    Arez.context().autorun( () -> {
      // Perform observation
      model1.getName();
      callCount.incrementAndGet();
    } );

    safeAction( () -> assertEquals( model1.getName(), name ) );
    assertEquals( callCount.get(), 1 );

    Disposable.dispose( model2 );

    safeAction( () -> assertEquals( model1.getName(), "X" ) );
    assertEquals( callCount.get(), 2 );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    static Model1 create( @Nonnull final String name )
    {
      return new AllowWriteInDisposeIntegrationTest_Arez_Model1( name );
    }

    @Observable
    @Nonnull
    abstract String getName();

    abstract void setName( @Nonnull String name );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
    @Nonnull
    private Model1 _other;

    @Nonnull
    static Model2 create( @Nonnull final Model1 other )
    {
      return new AllowWriteInDisposeIntegrationTest_Arez_Model2( other );
    }

    Model2( @Nonnull final Model1 other )
    {
      _other = other;
    }

    @PreDispose
    final void preDispose()
    {
      _other.setName( "X" );
    }
  }
}
