package arez.integration.action;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionSkippedEvent;
import java.util.function.IntSupplier;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class SkipIfDisposedIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class ExplicitSkipEnabledComponent
  {
    int invokeCount;

    @Action( skipIfDisposed = Feature.ENABLE )
    void myAction( @SuppressWarnings( "unused" ) final int value )
    {
      invokeCount++;
    }
  }

  @ArezComponent( defaultSkipIfDisposed = Feature.ENABLE )
  static abstract class DefaultSkipEnabledComponent
  {
    int invokeCount;

    @Action
    void myAction( @SuppressWarnings( "unused" ) final int value )
    {
      invokeCount++;
    }
  }

  @ArezComponent( defaultSkipIfDisposed = Feature.ENABLE )
  static abstract class DefaultSkipEnabledExplicitDisableComponent
  {
    int invokeCount;

    @Action( skipIfDisposed = Feature.DISABLE )
    void myAction( @SuppressWarnings( "unused" ) final int value )
    {
      invokeCount++;
    }
  }

  @ArezComponent( defaultSkipIfDisposed = Feature.AUTODETECT )
  static abstract class DefaultSkipAutodetectComponent
  {
    int invokeCount;

    @Action
    void myAction( @SuppressWarnings( "unused" ) final int value )
    {
      invokeCount++;
    }
  }

  @ArezComponent( defaultSkipIfDisposed = Feature.DISABLE )
  static abstract class DefaultSkipDisabledExplicitEnableComponent
  {
    int invokeCount;

    @Action( skipIfDisposed = Feature.ENABLE )
    void myAction( @SuppressWarnings( "unused" ) final int value )
    {
      invokeCount++;
    }
  }

  @Test
  public void explicitEnable_skipsDisposedAction()
  {
    final ExplicitSkipEnabledComponent component =
      new SkipIfDisposedIntegrationTest_Arez_ExplicitSkipEnabledComponent();
    Disposable.dispose( component );
    assertTrue( Disposable.isDisposed( component ) );
    assertActionSkipped( () -> component.myAction( 123 ), () -> component.invokeCount );
  }

  @Test
  public void componentDefaultEnable_skipsDisposedAction()
  {
    final DefaultSkipEnabledComponent component =
      new SkipIfDisposedIntegrationTest_Arez_DefaultSkipEnabledComponent();
    Disposable.dispose( component );
    assertTrue( Disposable.isDisposed( component ) );
    assertActionSkipped( () -> component.myAction( 123 ), () -> component.invokeCount );
  }

  @Test
  public void explicitDisable_overridesComponentDefaultEnable()
  {
    final DefaultSkipEnabledExplicitDisableComponent component =
      new SkipIfDisposedIntegrationTest_Arez_DefaultSkipEnabledExplicitDisableComponent();
    Disposable.dispose( component );
    assertTrue( Disposable.isDisposed( component ) );
    assertDisposedInvariant( () -> component.myAction( 123 ), component, () -> component.invokeCount );
  }

  @Test
  public void componentDefaultAutodetect_treatedAsDisable()
  {
    final DefaultSkipAutodetectComponent component =
      new SkipIfDisposedIntegrationTest_Arez_DefaultSkipAutodetectComponent();
    Disposable.dispose( component );
    assertTrue( Disposable.isDisposed( component ) );
    assertDisposedInvariant( () -> component.myAction( 123 ), component, () -> component.invokeCount );
  }

  @Test
  public void explicitEnable_overridesComponentDefaultDisable()
  {
    final DefaultSkipDisabledExplicitEnableComponent component =
      new SkipIfDisposedIntegrationTest_Arez_DefaultSkipDisabledExplicitEnableComponent();
    Disposable.dispose( component );
    assertTrue( Disposable.isDisposed( component ) );
    assertActionSkipped( () -> component.myAction( 123 ), () -> component.invokeCount );
  }

  private void assertActionSkipped( final Runnable action, final IntSupplier invokeCountSupplier )
  {
    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    Arez.context().getSpy().addSpyEventHandler( recorder );

    action.run();
    assertEquals( invokeCountSupplier.getAsInt(), 0 );

    recorder.assertEventCount( 1 );
    recorder.assertNextEvent( ActionSkippedEvent.class, e -> {
      assertFalse( e.isTracked() );
      assertEquals( e.getParameters().length, 1 );
      assertEquals( e.getParameters()[ 0 ], 123 );
    } );
  }

  private void assertDisposedInvariant( final Runnable action, final Object component, final IntSupplier invokeCountSupplier )
  {
    final IllegalStateException exception = expectThrows( IllegalStateException.class, action::run );
    final String componentName = component.toString().replace( "ArezComponent[", "" ).replace( "]", "" );
    assertEquals( exception.getMessage(),
                  "Method named 'myAction' invoked on disposed component named '" + componentName + "'" );
    assertEquals( invokeCountSupplier.getAsInt(), 0 );
  }
}
