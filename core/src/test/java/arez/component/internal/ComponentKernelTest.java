package arez.component.internal;

import arez.AbstractTest;
import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Component;
import arez.Disposable;
import arez.Observer;
import arez.SafeFunction;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComponentKernelTest
  extends AbstractTest
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
    assertFalse( kernel.hasOnDisposeListeners() );

    assertTrue( kernel.hasBeenInitialized() );
    assertFalse( kernel.hasBeenConstructed() );
    assertFalse( kernel.hasBeenCompleted() );
    assertFalse( kernel.isActive() );
    assertFalse( kernel.isReady() );
    assertFalse( kernel.isDisposing() );
    assertFalse( kernel.isDisposed() );
    assertEquals( kernel.describeState(), "initialized" );

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
    }, Observer.Flags.RUN_LATER );

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
    }, Observer.Flags.RUN_LATER );

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
    assertDefaultToString( kernel );

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

    assertFalse( context.safeAction( (SafeFunction<Boolean>) kernel::observe, ActionFlags.NO_VERIFY_ACTION_REQUIRED ) );

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

    assertFalse( context.safeAction( (SafeFunction<Boolean>) kernel::observe, ActionFlags.NO_VERIFY_ACTION_REQUIRED ) );

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
    kernel.addOnDisposeListener( "X", () -> {
      tasks.add( "OnDisposeListener" );
      assertEquals( kernel.describeState(), "disposing" );
    } );

    Disposable.dispose( kernel );

    assertEquals( String.join( ",", tasks ),
                  "PreDispose,OnDisposeListener,Dispose,PostDispose" );
    assertTrue( kernel.isDisposed() );
  }

  @Test
  public void addOnDisposeListener()
  {
    final ArezContext context = Arez.context();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType", 1 ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    assertEquals( kernel.getOnDisposeListeners().size(), 0 );
    assertEquals( callCount.get(), 0 );

    kernel.addOnDisposeListener( key, callCount::incrementAndGet );

    assertEquals( kernel.getOnDisposeListeners().size(), 1 );
    assertEquals( callCount.get(), 0 );

    kernel.getOnDisposeListeners().get( key ).call();

    assertEquals( kernel.getOnDisposeListeners().size(), 1 );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void addOnDisposeListener_afterDispose()
  {
    final ArezContext context = Arez.context();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType", 1 ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );

    kernel.dispose();

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    assertInvariantFailure( () -> kernel.addOnDisposeListener( key, callCount::incrementAndGet ),
                            "Arez-0170: Attempting to add OnDispose listener but ComponentKernel has been disposed." );
  }

  @Test
  public void addOnDisposeListener_duplicate()
  {
    final ArezContext context = Arez.context();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType", 1 ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    kernel.addOnDisposeListener( key, callCount::incrementAndGet );

    assertInvariantFailure( () -> kernel.addOnDisposeListener( key, callCount::incrementAndGet ),
                            "Arez-0166: Attempting to add OnDispose listener with key '" + key +
                            "' but a listener with that key already exists." );
  }

  @Test
  public void removeOnDisposeListener()
  {
    final ArezContext context = Arez.context();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType", 1 ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    assertEquals( kernel.getOnDisposeListeners().size(), 0 );

    kernel.addOnDisposeListener( key, callCount::incrementAndGet );

    assertEquals( kernel.getOnDisposeListeners().size(), 1 );

    kernel.removeOnDisposeListener( key );

    assertEquals( kernel.getOnDisposeListeners().size(), 0 );
  }

  @Test
  public void removeOnDisposeListener_after_dispose()
  {
    final ArezContext context = Arez.context();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType", 1 ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    kernel.addOnDisposeListener( key, callCount::incrementAndGet );

    assertEquals( kernel.getOnDisposeListeners().size(), 1 );

    kernel.dispose();

    // Perfectly legitimate to remove after dispose and can occur in certain application sequences
    kernel.removeOnDisposeListener( key );

    assertEquals( kernel.getOnDisposeListeners().size(), 0 );
  }

  @Test
  public void removeOnDisposeListener_notPresent()
  {
    final ArezContext context = Arez.context();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType", 1 ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );

    final String key = ValueUtil.randomString();
    assertInvariantFailure( () -> kernel.removeOnDisposeListener( key ),
                            "Arez-0167: Attempting to remove OnDispose listener with key '" + key +
                            "' but no such listener exists." );
  }

  @Test
  public void dispose()
  {
    final ArezContext context = Arez.context();
    final AtomicReference<ComponentKernel> ref = new AtomicReference<>();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType",
                                              1,
                                              "MyType@1",
                                              () -> ref.get().notifyOnDisposeListeners() ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );
    ref.set( kernel );

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    assertEquals( callCount.get(), 0 );

    kernel.addOnDisposeListener( key, callCount::incrementAndGet );

    assertEquals( callCount.get(), 0 );

    kernel.dispose();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void notifyOnDisposeOperation()
  {
    final ArezContext context = Arez.context();
    final AtomicReference<ComponentKernel> ref = new AtomicReference<>();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType",
                                              1,
                                              "MyType@1",
                                              () -> ref.get().notifyOnDisposeListeners() ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );
    ref.set( kernel );
    /*
     * Three listeners required in test. Two that are disposed
     * concurrently and one that is disposed ahead of time
     */

    final String key1 = ValueUtil.randomString();
    final String key2 = ValueUtil.randomString();
    final String key3 = ValueUtil.randomString();
    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );
    assertEquals( callCount3.get(), 0 );

    kernel.addOnDisposeListener( key1, callCount1::incrementAndGet );
    kernel.addOnDisposeListener( key2, callCount2::incrementAndGet );
    kernel.addOnDisposeListener( key3, callCount3::incrementAndGet );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );
    assertEquals( callCount3.get(), 0 );

    kernel.removeOnDisposeListener( key2 );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );
    assertEquals( callCount3.get(), 0 );

    assertFalse( kernel.isDisposed() );
    kernel.dispose();
    assertTrue( kernel.isDisposed() );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 0 );
    assertEquals( callCount3.get(), 1 );

    // No-op
    kernel.dispose();
  }

  @Test
  public void disposeWhenAListenerHasDisposedKey()
  {
    final ArezContext context = Arez.context();
    final AtomicReference<ComponentKernel> ref = new AtomicReference<>();
    final ComponentKernel kernel =
      new ComponentKernel( context,
                           "MyType",
                           1,
                           context.component( "MyType",
                                              1,
                                              "MyType@1",
                                              () -> ref.get().notifyOnDisposeListeners() ),
                           null,
                           null,
                           null,
                           true,
                           false,
                           false );
    ref.set( kernel );

    final String key1 = ValueUtil.randomString();
    final Disposable key2 = new Disposable()
    {
      @Override
      public void dispose()
      {
      }

      @Override
      public boolean isDisposed()
      {
        return true;
      }
    };
    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    kernel.addOnDisposeListener( key1, callCount1::incrementAndGet );
    kernel.addOnDisposeListener( key2, callCount2::incrementAndGet );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    assertFalse( kernel.isDisposed() );
    kernel.dispose();
    assertTrue( kernel.isDisposed() );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 0 );
  }
}
