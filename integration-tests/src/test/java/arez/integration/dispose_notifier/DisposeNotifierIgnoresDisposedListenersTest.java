package arez.integration.dispose_notifier;

import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.annotations.PreDispose;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class DisposeNotifierIgnoresDisposedListenersTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final String name = ValueUtil.randomString();
    final Model1 model1 = Model1.create( name );
    final Model2 model2 = Model2.create( model1 );

    Disposable.dispose( model2 );

    assertTrue( Disposable.isDisposed( model1 ) );
    assertTrue( Disposable.isDisposed( model2 ) );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    static Model1 create( @Nonnull final String name )
    {
      return new DisposeNotifierIgnoresDisposedListenersTest_Arez_Model1( name );
    }

    @Observable
    @Nonnull
    abstract String getName();

    abstract void setName( @Nonnull String name );
  }

  @ArezComponent
  static abstract class Model2
  {
    @CascadeDispose
    Model1 _other;

    @Nonnull
    static Model2 create( @Nullable final Model1 other )
    {
      return new DisposeNotifierIgnoresDisposedListenersTest_Arez_Model2( other );
    }

    Model2( @Nullable final Model1 other )
    {
      _other = other;
    }

    @PreDispose
    void preDispose()
    {
      final Model1 other = _other;
      if ( null != other )
      {
        Disposable.dispose( other );
      }
    }

    @Action
    void clearOther()
    {
      Disposable.dispose( getOther() );
    }

    @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
    @Observable
    Model1 getOther()
    {
      return _other;
    }

    void setOther( final Model1 other )
    {
      _other = other;
    }
  }
}
