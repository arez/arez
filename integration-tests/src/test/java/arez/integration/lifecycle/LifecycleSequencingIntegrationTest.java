package arez.integration.lifecycle;

import arez.ArezTestUtil;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import arez.annotations.PostDispose;
import arez.annotations.PreDispose;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class LifecycleSequencingIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final Model1 model = Model1.create();

    Disposable.dispose( model );

    assertEquals( String.join( " ", model._steps ), "Constructor PostConstruct PreDispose PostDispose" );
  }

  @Test
  public void scenario_NativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();
    final Model2 model = Model2.create();

    Disposable.dispose( model );

    assertEquals( String.join( " ", model._steps ), "Constructor PostConstruct PreDispose PostDispose" );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model1
  {
    final ArrayList<String> _steps = new ArrayList<>();

    @Nonnull
    static Model1 create()
    {
      return new LifecycleSequencingIntegrationTest_Arez_Model1();
    }

    Model1()
    {
      _steps.add( "Constructor" );
    }

    @PostConstruct
    final void postConstruct()
    {
      _steps.add( "PostConstruct" );
    }

    @PreDispose
    final void preDispose()
    {
      _steps.add( "PreDispose" );
    }

    @PostDispose
    final void postDispose()
    {
      _steps.add( "PostDispose" );
    }
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
    final ArrayList<String> _steps = new ArrayList<>();

    @Nonnull
    static Model2 create()
    {
      return new LifecycleSequencingIntegrationTest_Arez_Model2();
    }

    Model2()
    {
      _steps.add( "Constructor" );
    }

    @PostConstruct
    final void postConstruct()
    {
      _steps.add( "PostConstruct" );
    }

    @PreDispose
    final void preDispose()
    {
      _steps.add( "PreDispose" );
    }

    @PostDispose
    final void postDispose()
    {
      _steps.add( "PostDispose" );
    }
  }
}
