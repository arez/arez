package arez.component.internal;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.Observer;
import arez.spy.ComponentInfo;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "unchecked" )
public class MemoizeCacheTest
  extends AbstractArezTest
{
  @BeforeMethod
  @Override
  protected void beforeTest()
    throws Exception
  {
    super.beforeTest();
    ArezTestUtil.disableZones();
  }

  @Test
  public void basicOperation()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final MemoizeCache.Function<String> function = args -> {
      observeADependency();
      callCount.incrementAndGet();
      return args[ 0 ] + "." + args[ 1 ];
    };
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, name, function, 2, Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    assertFalse( cache.isDisposed() );
    assertEquals( cache.getNextIndex(), 0 );

    assertEquals( cache.getFlags(), Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    final Observer observer1 = context.observer( () -> {
      observeADependency();
      assertEquals( cache.get( "a", "b" ), "a.b" );
    } );
    assertEquals( callCount.get(), 1 );
    assertEquals( cache.getNextIndex(), 1 );
    assertEquals( cache.getCache().size(), 1 );
    assertEquals( ( (Map) cache.getCache().get( "a" ) ).size(), 1 );
    final ComputableValue<String> computableValue1 =
      (ComputableValue<String>) ( (Map) cache.getCache().get( "a" ) ).get( "b" );
    assertNotNull( computableValue1 );
    assertEquals( context.safeAction( computableValue1::get ), "a.b" );
    final Observer observer2 = context.observer( () -> {
      observeADependency();
      assertEquals( cache.get( "a", "b" ), "a.b" );
    } );
    assertEquals( callCount.get(), 1 );
    assertEquals( cache.getNextIndex(), 1 );
    assertEquals( cache.getCache().size(), 1 );
    assertEquals( ( (Map) cache.getCache().get( "a" ) ).size(), 1 );
    final Observer observer3 = context.observer( () -> {
      observeADependency();
      assertEquals( cache.get( "a", "c" ), "a.c" );
    } );
    assertEquals( callCount.get(), 2 );
    assertEquals( cache.getNextIndex(), 2 );
    assertEquals( cache.getCache().size(), 1 );
    assertEquals( ( (Map) cache.getCache().get( "a" ) ).size(), 2 );
    final ComputableValue<String> computableValue2 =
      (ComputableValue<String>) ( (Map) cache.getCache().get( "a" ) ).get( "c" );
    assertNotNull( computableValue2 );
    assertEquals( context.safeAction( computableValue2::get ), "a.c" );

    observer1.dispose();
    assertEquals( callCount.get(), 2 );
    assertEquals( cache.getNextIndex(), 2 );
    assertEquals( cache.getCache().size(), 1 );
    assertFalse( computableValue1.isDisposed() );
    assertFalse( computableValue2.isDisposed() );
    assertEquals( ( (Map) cache.getCache().get( "a" ) ).size(), 2 );

    observer2.dispose();
    assertEquals( callCount.get(), 2 );
    assertEquals( cache.getNextIndex(), 2 );
    assertEquals( cache.getCache().size(), 1 );
    assertTrue( computableValue1.isDisposed() );
    assertFalse( computableValue2.isDisposed() );
    assertEquals( ( (Map) cache.getCache().get( "a" ) ).size(), 1 );

    observer3.dispose();
    assertEquals( callCount.get(), 2 );
    assertEquals( cache.getNextIndex(), 2 );
    assertTrue( computableValue1.isDisposed() );
    assertTrue( computableValue2.isDisposed() );
    assertEquals( cache.getCache().size(), 0 );
  }

  @Test
  public void getComputableValue_recreatesDisposedElements()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final MemoizeCache.Function<String> function = args -> {
      observeADependency();
      callCount.incrementAndGet();
      return args[ 0 ] + "." + args[ 1 ];
    };
    final String name = ValueUtil.randomString();
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, name, function, 2 );

    final ComputableValue<String> computableValue1 = cache.getComputableValue( "a", "b" );
    final ComputableValue<String> computableValue1b = cache.getComputableValue( "a", "b" );

    assertFalse( computableValue1.isDisposed() );
    assertEquals( computableValue1, computableValue1b );

    computableValue1.dispose();

    assertTrue( computableValue1.isDisposed() );

    final ComputableValue<String> computableValue2 = cache.getComputableValue( "a", "b" );
    final ComputableValue<String> computableValue2b = cache.getComputableValue( "a", "b" );

    assertFalse( computableValue2.isDisposed() );
    assertNotEquals( computableValue2, computableValue1 );
    assertEquals( computableValue2, computableValue2b );
  }

  @Test
  public void basicOperationWhenNativeComponentPresent()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final MemoizeCache.Function<String> function = args -> {
      observeADependency();
      callCount.incrementAndGet();
      return args[ 0 ] + "." + args[ 1 ];
    };
    final ArezContext context = Arez.context();
    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString() );

    final String name = ValueUtil.randomString();
    final MemoizeCache<String> cache = new MemoizeCache<>( null, component, name, function, 2 );

    final ComputableValue<String> computableValue1 = cache.getComputableValue( "a", "b" );

    assertFalse( computableValue1.isDisposed() );

    final ComponentInfo componentInfo = context.getSpy().asComputableValueInfo( computableValue1 ).getComponent();
    assertNotNull( componentInfo );
    assertEquals( componentInfo.getName(), component.getName() );

    component.dispose();

    assertTrue( computableValue1.isDisposed() );
  }

  @Test
  public void disposingCacheClearsOutComputableValues()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final MemoizeCache.Function<String> function = args -> {
      observeADependency();
      callCount.incrementAndGet();
      return args[ 0 ] + "." + args[ 1 ];
    };
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final MemoizeCache<String> cache = new MemoizeCache<>( null, null, name, function, 2 );

    assertFalse( cache.isDisposed() );
    assertEquals( cache.getNextIndex(), 0 );

    context.observer( () -> {
      observeADependency();
      if ( Disposable.isNotDisposed( cache ) )
      {
        assertEquals( cache.get( "a", "b" ), "a.b" );
      }
    } );
    assertEquals( callCount.get(), 1 );
    assertEquals( cache.getNextIndex(), 1 );
    assertEquals( cache.getCache().size(), 1 );

    final ComputableValue<String> computableValue1 =
      (ComputableValue<String>) ( (Map) cache.getCache().get( "a" ) ).get( "b" );
    assertNotNull( computableValue1 );

    assertFalse( computableValue1.isDisposed() );

    cache.dispose();

    assertEquals( callCount.get(), 1 );
    assertEquals( cache.getNextIndex(), 1 );
    assertEquals( cache.getCache().size(), 0 );
    assertTrue( computableValue1.isDisposed() );
  }

  @Test
  public void basicOperation_inAction()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final MemoizeCache.Function<String> function = args -> {
      observeADependency();
      callCount.incrementAndGet();
      return args[ 0 ] + "." + args[ 1 ];
    };
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final MemoizeCache<String> cache = new MemoizeCache<>( null, null, name, function, 2 );

    assertEquals( cache.getNextIndex(), 0 );

    assertEquals( context.safeAction( () -> cache.get( "a", "b" ) ), "a.b" );

    assertEquals( callCount.get(), 1 );
    assertEquals( cache.getNextIndex(), 1 );

    // Not observed so should be cleaned up immediately
    assertEquals( cache.getCache().size(), 0 );
  }

  @Test
  public void disposeComputableValue_passedBadArgCounts()
  {
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, ValueUtil.randomString(), args -> args[ 0 ] + "." + args[ 1 ], 2 );

    assertInvariantFailure( () -> cache.disposeComputableValue( "a" ),
                            "Arez-0163: MemoizeCache.disposeComputableValue called with 1 argument(s) but expected 2 argument(s)." );
  }

  @Test
  public void disposeComputableValue_noComputableValueCachedForArgs()
  {
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, ValueUtil.randomString(), args -> args[ 0 ] + "." + args[ 1 ], 1 );

    assertInvariantFailure( () -> cache.disposeComputableValue( "a" ),
                            "Arez-0193: MemoizeCache.disposeComputableValue called with args [a] but unable to locate corresponding ComputableValue." );
  }

  @Test
  public void get_passedBadArgCounts()
  {
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, ValueUtil.randomString(), args -> args[ 0 ] + "." + args[ 1 ], 2 );

    assertInvariantFailure( () -> cache.get( "a" ),
                            "Arez-0162: MemoizeCache.getComputableValue called with 1 arguments but expected 2 arguments." );
  }

  @Test
  public void get_invokedOnDisposed()
  {
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, "X", args -> args[ 0 ] + "." + args[ 1 ], 2 );

    cache.dispose();

    assertInvariantFailure( () -> cache.get( "a", "b" ),
                            "Arez-0161: MemoizeCache named 'X' had get() invoked when disposed." );
  }

  @Test
  public void constructorPassedName_whenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    assertInvariantFailure( () -> new MemoizeCache<>( null, null, "X", args -> args[ 0 ], 1 ),
                            "Arez-0159: MemoizeCache passed a name 'X' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void constructorPassedBadArgCount()
  {
    assertInvariantFailure( () -> new MemoizeCache<>( null, null, "X", args -> args[ 0 ], 0 ),
                            "Arez-0160: MemoizeCache constructed with invalid argCount: 0. Expected positive value." );
  }

  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();
    assertInvariantFailure( () -> new MemoizeCache<>( Arez.context(), null, null, args -> args[ 0 ], 1 ),
                            "Arez-174: MemoizeCache passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void constructorPassedBadFlags()
  {
    assertInvariantFailure( () -> new MemoizeCache<>( null, null, "X", args -> args[ 0 ], 1, Flags.KEEPALIVE ),
                            "Arez-0211: MemoizeCache passed unsupported flags. Unsupported bits: " + Flags.KEEPALIVE );
  }
}
