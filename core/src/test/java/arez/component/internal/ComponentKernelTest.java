package arez.component.internal;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Component;
import arez.Disposable;
import arez.Flags;
import arez.Observer;
import arez.SafeFunction;
import arez.component.DisposeNotifier;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentKernelTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final int id = ValueUtil.nextID();
    final Component component = context.component( "MyType", id );

    final ComponentKernel kernel =
      new ComponentKernel( context, name, id, component, null, null, null, false, false, false );
    assertEquals( kernel.getContext(), context );
    assertEquals( kernel.getName(), name );
    assertEquals( kernel.getId(), id );
    assertEquals( kernel.getComponent(), component );
    assertEquals( kernel.toString(), name );

    assertTrue( kernel.hasBeenInitialized() );
    assertFalse( kernel.hasBeenConstructed() );
    assertFalse( kernel.hasBeenCompleted() );
    assertFalse( kernel.isActive() );
    assertFalse( kernel.isReady() );
    assertFalse( kernel.isDisposed() );
    assertEquals( kernel.describeState(), "initialized" );

    assertInvariantFailure( kernel::getDisposeNotifier,
                            "Arez-0217: ComponentKernel.getDisposeNotifier() invoked when no notifier is " +
                            "associated with the component named '" + name + "'." );

    kernel.componentConstructed();

    assertTrue( kernel.hasBeenInitialized() );
    assertTrue( kernel.hasBeenConstructed() );
    assertFalse( kernel.hasBeenCompleted() );
    assertTrue( kernel.isActive() );
    assertFalse( kernel.isReady() );
    assertFalse( kernel.isDisposed() );
    assertEquals( kernel.describeState(), "constructed" );

    // create observer that will run during componentComplete() because we declare RUN_LATER here
    context.observer( () -> {
      context.observable().reportObserved();
      assertTrue( kernel.hasBeenInitialized() );
      assertTrue( kernel.hasBeenConstructed() );
      assertTrue( kernel.hasBeenCompleted() );
      assertTrue( kernel.isActive() );
      assertFalse( kernel.isReady() );
      assertFalse( kernel.isDisposed() );
      assertEquals( kernel.describeState(), "complete" );
    }, Flags.RUN_LATER );

    kernel.componentComplete();

    assertTrue( kernel.hasBeenInitialized() );
    assertTrue( kernel.hasBeenConstructed() );
    assertTrue( kernel.hasBeenCompleted() );
    assertTrue( kernel.isActive() );
    assertTrue( kernel.isReady() );
    assertFalse( kernel.isDisposed() );
    assertEquals( kernel.describeState(), "ready" );

    kernel.dispose();

    assertTrue( kernel.hasBeenInitialized() );
    assertTrue( kernel.hasBeenConstructed() );
    assertTrue( kernel.hasBeenCompleted() );
    assertFalse( kernel.isActive() );
    assertFalse( kernel.isReady() );
    assertTrue( kernel.isDisposed() );
    assertEquals( kernel.describeState(), "disposed" );
  }

  @Test
  public void basicOperation_SkipTransitionToComplete()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final int id = ValueUtil.nextID();
    final Component component = context.component( "MyType", id );

    final ComponentKernel kernel =
      new ComponentKernel( context, name, id, component, null, null, null, false, false, false );
    kernel.componentConstructed();

    assertTrue( kernel.hasBeenInitialized() );
    assertTrue( kernel.hasBeenConstructed() );
    assertFalse( kernel.hasBeenCompleted() );
    assertTrue( kernel.isActive() );
    assertFalse( kernel.isReady() );
    assertFalse( kernel.isDisposed() );
    assertEquals( kernel.describeState(), "constructed" );

    final AtomicInteger callCount = new AtomicInteger();

    // create observer that will run during componentComplete() because we declare RUN_LATER here
    context.observer( () -> {
      context.observable().reportObserved();
      callCount.incrementAndGet();
    }, Flags.RUN_LATER );

    assertEquals( callCount.get(), 0 );

    kernel.componentReady();

    assertEquals( callCount.get(), 0 );

    assertEquals( kernel.describeState(), "ready" );
  }

  @Test
  public void noNameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();
    final int id = ValueUtil.nextID();
    final Component component = context.component( "MyType", id );

    final ComponentKernel kernel =
      new ComponentKernel( context, null, id, component, null, null, null, false, false, false );

    assertEquals( kernel.getContext(), context );
    assertTrue( kernel.toString().startsWith( kernel.getClass().getName() + "@" ), "kernel.toString() == " + kernel );

    assertInvariantFailure( kernel::getName,
                            "Arez-0164: ComponentKernel.getName() invoked when Arez.areNamesEnabled() returns false." );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();
    final int id = ValueUtil.nextID();
    final Component component = context.component( "MyType", id );
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new ComponentKernel( context,
                                                       name,
                                                       id,
                                                       component,
                                                       null,
                                                       null,
                                                       null,
                                                       false,
                                                       false,
                                                       false ),
                            "Arez-0156: ComponentKernel passed a name '" +
                            name + "' but Arez.areNamesEnabled() returns false." );
  }

  @Test
  public void contextSuppliedWhenZonesDisabled()
  {
    ArezTestUtil.disableZones();

    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final int id = ValueUtil.nextID();
    final Component component = context.component( "MyType", id );

    assertInvariantFailure( () -> new ComponentKernel( context,
                                                       name,
                                                       id,
                                                       component,
                                                       null,
                                                       null,
                                                       null,
                                                       false,
                                                       false,
                                                       false ),
                            "Arez-0100: ComponentKernel passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void getComponent_whenComponentNotEnabled()
  {
    ArezTestUtil.disableNativeComponents();

    final String name = ValueUtil.randomString();

    final ComponentKernel kernel =
      new ComponentKernel( Arez.context(), name, ValueUtil.nextID(), null, null, null, null, false, false, false );

    assertInvariantFailure( kernel::getComponent,
                            "Arez-0216: ComponentKernel.getComponent() invoked when " +
                            "Arez.areNativeComponentsEnabled() returns false on component named '" + name + "'." );
  }

  @Test
  public void getId_NoSyntheticId()
  {
    ArezTestUtil.disableNativeComponents();

    final String name = ValueUtil.randomString();

    final ComponentKernel kernel =
      new ComponentKernel( Arez.context(), name, 0, null, null, null, null, false, false, false );

    assertInvariantFailure( kernel::getId,
                            "Arez-0213: Attempted to unexpectedly invoke ComponentKernel.getId() " +
                            "method to access synthetic id on component named '" + name + "'." );
  }

  @Test
  public void misAlignedComponentId()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final int id = 23;
    final Component component = context.component( "MyType", "Blah" );

    assertInvariantFailure( () -> new ComponentKernel( context,
                                                       name,
                                                       id,
                                                       component,
                                                       null,
                                                       null,
                                                       null,
                                                       false,
                                                       false,
                                                       false ),
                            "Arez-0222: ComponentKernel named '" + name + "' passed an id 23 and " +
                            "a component but the component had a different id (Blah)" );
  }

  @Test
  public void componentConstructed_BadTransform()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final Component component = context.component( "MyType", 0 );

    final ComponentKernel kernel =
      new ComponentKernel( context, name, 0, component, null, null, null, false, false, false );

    kernel.componentConstructed();

    assertEquals( kernel.describeState(), "constructed" );

    assertInvariantFailure( kernel::componentConstructed,
                            "Arez-0219: Bad state transition from constructed to constructed " +
                            "on component named '" + name + "'." );
  }

  @Test
  public void componentComplete_BadTransform()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final Component component = context.component( "MyType", 0 );

    final ComponentKernel kernel =
      new ComponentKernel( context, name, 0, component, null, null, null, false, false, false );

    assertInvariantFailure( kernel::componentComplete,
                            "Arez-0220: Bad state transition from initialized to complete " +
                            "on component named '" + name + "'." );
  }

  @Test
  public void componentReady_BadTransform()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final Component component = context.component( "MyType", 0 );

    final ComponentKernel kernel =
      new ComponentKernel( context, name, 0, component, null, null, null, false, false, false );

    assertInvariantFailure( kernel::componentReady,
                            "Arez-0218: Bad state transition from initialized to ready on component named '" +
                            name +
                            "'." );
  }

  @Test
  public void observe_noObservable()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final Component component = context.component( "MyType", 0 );

    final ComponentKernel kernel =
      new ComponentKernel( context, name, 0, component, null, null, null, false, false, false );

    assertInvariantFailure( kernel::observe,
                            "Arez-0221: ComponentKernel.observe() invoked on component named '" +
                            name + "' but observing is not enabled for component." );
  }

  @Test
  public void observe()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final ComponentKernel kernel =
      new ComponentKernel( context, name, 0, null, null, null, null, false, true, false );

    kernel.componentConstructed();
    kernel.componentReady();

    assertTrue( context.safeAction( (SafeFunction<Boolean>) kernel::observe ) );

    final AtomicInteger disposedCallCount = new AtomicInteger();
    final AtomicInteger notDisposedCallCount = new AtomicInteger();
    context.observer( () -> {
      context.observable().reportObserved();
      if ( kernel.isDisposed() )
      {
        disposedCallCount.incrementAndGet();
        assertFalse( kernel.observe() );
      }
      else
      {
        notDisposedCallCount.incrementAndGet();
        assertTrue( kernel.observe() );
      }
    } );

    assertEquals( notDisposedCallCount.get(), 1 );
    assertEquals( disposedCallCount.get(), 0 );

    Disposable.dispose( kernel );

    assertFalse( context.safeAction( (SafeFunction<Boolean>) kernel::observe, Flags.NO_VERIFY_ACTION_REQUIRED ) );

    assertEquals( notDisposedCallCount.get(), 1 );
    assertEquals( disposedCallCount.get(), 1 );
  }

  @Test
  public void disposeOnDeactivate()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final ComponentKernel kernel =
      new ComponentKernel( context, name, 0, null, null, null, null, false, true, true );

    kernel.componentConstructed();
    kernel.componentReady();

    final AtomicInteger disposedCallCount = new AtomicInteger();
    final AtomicInteger notDisposedCallCount = new AtomicInteger();
    final Observer observer = context.observer( () -> {
      context.observable().reportObserved();
      if ( kernel.isDisposed() )
      {
        disposedCallCount.incrementAndGet();
        assertFalse( kernel.observe() );
      }
      else
      {
        notDisposedCallCount.incrementAndGet();
        assertTrue( kernel.observe() );
      }
    } );

    assertTrue( context.safeAction( (SafeFunction<Boolean>) kernel::observe ) );

    assertEquals( notDisposedCallCount.get(), 1 );
    assertEquals( disposedCallCount.get(), 0 );

    // Dispose the observer - this should trigger the dispose of the ComponentKernel
    Disposable.dispose( observer );

    assertFalse( context.safeAction( (SafeFunction<Boolean>) kernel::observe, Flags.NO_VERIFY_ACTION_REQUIRED ) );

    assertEquals( notDisposedCallCount.get(), 1 );

    // Observer has been disposed so it never gets dispose action
    assertEquals( disposedCallCount.get(), 0 );
    assertTrue( kernel.isDisposed() );
  }

  @Test
  public void disposeSequencing()
  {
    ArezTestUtil.disableNativeComponents();

    final ArrayList<String> tasks = new ArrayList<>();

    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final ComponentKernel kernel =
      new ComponentKernel( context,
                           name,
                           0,
                           null,
                           () -> tasks.add( "PreDispose" ),
                           () -> tasks.add( "Dispose" ),
                           () -> tasks.add( "PostDispose" ),
                           true,
                           false,
                           false );

    kernel.componentConstructed();
    kernel.componentReady();
    final DisposeNotifier disposeNotifier = kernel.getDisposeNotifier();
    assertNotNull( disposeNotifier );
    disposeNotifier.addOnDisposeListener( "X", () -> {
      tasks.add( "OnDisposeListener" );
      assertEquals( kernel.describeState(), "disposing" );
    } );

    Disposable.dispose( kernel );

    assertEquals( String.join( ",", tasks ),
                  "PreDispose,OnDisposeListener,Dispose,PostDispose" );
    assertTrue( kernel.isDisposed() );
  }
}
