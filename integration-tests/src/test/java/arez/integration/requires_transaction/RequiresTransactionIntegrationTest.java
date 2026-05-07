package arez.integration.requires_transaction;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.ActionFlags;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;
import arez.annotations.TrackingMode;
import arez.annotations.TransactionMode;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.TestSpyEventHandler;
import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( { "UnusedReturnValue", "unused" } )
public final class RequiresTransactionIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class NoTransactionComponent
  {
    int _callCount;

    @RequiresTransaction
    void perform()
    {
      _callCount++;
    }
  }

  @Test
  public void outsideTransactionFails()
  {
    final NoTransactionComponent component = new RequiresTransactionIntegrationTest_Arez_NoTransactionComponent();

    assertInvariant( component::perform,
                     "Arez-0229: Method named 'perform' invoked outside of a transaction on component type 'arez_integration_requires_transaction_RequiresTransactionIntegrationTest_NoTransactionComponent'." );

    assertEquals( component._callCount, 0 );
  }

  @ArezComponent
  static abstract class ReadOnlyComponent
  {
    int _callCount;

    @RequiresTransaction( mode = TransactionMode.READ_ONLY )
    int perform()
    {
      return ++_callCount;
    }
  }

  @Test
  public void readOnlyModeMismatchFails()
  {
    final ReadOnlyComponent component = new RequiresTransactionIntegrationTest_Arez_ReadOnlyComponent();

    assertInvariant( () -> safeAction( component::perform ),
                     "Arez-0230: Method named 'perform' invoked with transaction mode READ_WRITE but expected READ_ONLY on component type 'arez_integration_requires_transaction_RequiresTransactionIntegrationTest_ReadOnlyComponent'." );

    assertEquals( component._callCount, 0 );
  }

  @Test
  public void modeRequirementDegradesWhenTransactionTypeEnforcementDisabled()
  {
    ArezTestUtil.noEnforceTransactionType();

    final ReadOnlyComponent component = new RequiresTransactionIntegrationTest_Arez_ReadOnlyComponent();

    Arez.context().safeAction( component::perform, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
    assertEquals( component._callCount, 1 );
  }

  @ArezComponent
  static abstract class TrackingComponent
  {
    int _callCount;

    @RequiresTransaction( tracking = TrackingMode.TRACKING )
    int perform()
    {
      return ++_callCount;
    }
  }

  @Test
  public void trackingMismatchFails()
  {
    final TrackingComponent component = new RequiresTransactionIntegrationTest_Arez_TrackingComponent();

    assertInvariant( () -> safeAction( component::perform ),
                     "Arez-0231: Method named 'perform' invoked with transaction tracking state NON_TRACKING but expected TRACKING on component type 'arez_integration_requires_transaction_RequiresTransactionIntegrationTest_TrackingComponent'." );

    assertEquals( component._callCount, 0 );
  }

  @Test
  public void trackingRequirementSucceedsInsideTrackingTransaction()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final TrackingComponent component = new RequiresTransactionIntegrationTest_Arez_TrackingComponent();
    final Observer tracker =
      context.tracker( "Tracker", () -> {}, Observer.Flags.AREZ_OR_NO_DEPENDENCIES );

    assertEquals( context.observe( tracker, component::perform ).intValue(), 1 );
    assertEquals( component._callCount, 1 );
  }

  @ArezComponent
  static abstract class SpyComponent
  {
    @RequiresTransaction
    void perform()
    {
    }
  }

  @Test
  public void emitsNoActionOrTransactionSpyEventsOfItsOwn()
  {
    final ArezContext context = Arez.context();
    final SpyComponent component = new RequiresTransactionIntegrationTest_Arez_SpyComponent();
    final TestSpyEventHandler recorder = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( recorder );

    context.safeAction( "OuterAction", component::perform, ActionFlags.NO_VERIFY_ACTION_REQUIRED );

    recorder.assertEventCount( 4 );
    recorder.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), "OuterAction" ) );
    recorder.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), "OuterAction" ) );
    recorder.assertNextEvent( TransactionCompleteEvent.class, e -> assertEquals( e.getName(), "OuterAction" ) );
    recorder.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), "OuterAction" ) );
  }

  static abstract class InheritedBase
  {
    int _callCount;

    @RequiresTransaction
    void performBase()
    {
      _callCount++;
    }
  }

  @ArezComponent
  static abstract class InheritedComponent
    extends InheritedBase
  {
  }

  @Test
  public void inheritedMethodDispatchesInsideTransaction()
  {
    final InheritedComponent component = new RequiresTransactionIntegrationTest_Arez_InheritedComponent();

    Arez.context().safeAction( component::performBase, ActionFlags.NO_VERIFY_ACTION_REQUIRED );

    assertEquals( component._callCount, 1 );
  }

  interface DefaultRequiresTransactionMethods
  {
    @RequiresTransaction
    default int performDefault()
    {
      return 7;
    }
  }

  @ArezComponent
  static abstract class DefaultMethodComponent
    implements DefaultRequiresTransactionMethods
  {
  }

  @Test
  public void defaultMethodDispatchesInsideTransaction()
  {
    final DefaultMethodComponent component = new RequiresTransactionIntegrationTest_Arez_DefaultMethodComponent();
    assertEquals( Arez.context()
                       .safeAction( component::performDefault, ActionFlags.NO_VERIFY_ACTION_REQUIRED )
                       .intValue(),
                  7 );
  }
}
