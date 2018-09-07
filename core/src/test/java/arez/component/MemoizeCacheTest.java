package arez.component;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Component;
import arez.ComputedValue;
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
    ArezTestUtil.resetState();
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

    assertEquals( cache.isDisposed(), false );
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
    final ComputedValue<String> computedValue1 =
      (ComputedValue<String>) ( (Map) cache.getCache().get( "a" ) ).get( "b" );
    assertNotNull( computedValue1 );
    assertEquals( context.safeAction( computedValue1::get ), "a.b" );
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
    final ComputedValue<String> computedValue2 =
      (ComputedValue<String>) ( (Map) cache.getCache().get( "a" ) ).get( "c" );
    assertNotNull( computedValue2 );
    assertEquals( context.safeAction( computedValue2::get ), "a.c" );

    observer1.dispose();
    assertEquals( callCount.get(), 2 );
    assertEquals( cache.getNextIndex(), 2 );
    assertEquals( cache.getCache().size(), 1 );
    assertEquals( computedValue1.isDisposed(), false );
    assertEquals( computedValue2.isDisposed(), false );
    assertEquals( ( (Map) cache.getCache().get( "a" ) ).size(), 2 );

    observer2.dispose();
    assertEquals( callCount.get(), 2 );
    assertEquals( cache.getNextIndex(), 2 );
    assertEquals( cache.getCache().size(), 1 );
    assertEquals( computedValue1.isDisposed(), true );
    assertEquals( computedValue2.isDisposed(), false );
    assertEquals( ( (Map) cache.getCache().get( "a" ) ).size(), 1 );

    observer3.dispose();
    assertEquals( callCount.get(), 2 );
    assertEquals( cache.getNextIndex(), 2 );
    assertEquals( computedValue1.isDisposed(), true );
    assertEquals( computedValue2.isDisposed(), true );
    assertEquals( cache.getCache().size(), 0 );
  }

  @Test
  public void getComputedValue_recreatesDisposedElements()
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

    final ComputedValue<String> computedValue1 = cache.getComputedValue( "a", "b" );
    final ComputedValue<String> computedValue1b = cache.getComputedValue( "a", "b" );

    assertEquals( computedValue1.isDisposed(), false );
    assertEquals( computedValue1, computedValue1b );

    computedValue1.dispose();

    assertEquals( computedValue1.isDisposed(), true );

    final ComputedValue<String> computedValue2 = cache.getComputedValue( "a", "b" );
    final ComputedValue<String> computedValue2b = cache.getComputedValue( "a", "b" );

    assertEquals( computedValue2.isDisposed(), false );
    assertNotEquals( computedValue2, computedValue1 );
    assertEquals( computedValue2, computedValue2b );
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

    final ComputedValue<String> computedValue1 = cache.getComputedValue( "a", "b" );

    assertEquals( computedValue1.isDisposed(), false );

    final ComponentInfo componentInfo = context.getSpy().asComputedValueInfo( computedValue1 ).getComponent();
    assertNotNull( componentInfo );
    assertEquals( componentInfo.getName(), component.getName() );

    component.dispose();

    assertEquals( computedValue1.isDisposed(), true );
  }

  @Test
  public void disposingCacheClearsOutComputedValues()
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

    assertEquals( cache.isDisposed(), false );
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

    final ComputedValue<String> computedValue1 =
      (ComputedValue<String>) ( (Map) cache.getCache().get( "a" ) ).get( "b" );
    assertNotNull( computedValue1 );

    assertEquals( computedValue1.isDisposed(), false );

    cache.dispose();

    assertEquals( callCount.get(), 1 );
    assertEquals( cache.getNextIndex(), 1 );
    assertEquals( cache.getCache().size(), 0 );
    assertEquals( computedValue1.isDisposed(), true );
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
  public void disposeComputedValue_passedBadArgCounts()
  {
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, ValueUtil.randomString(), args -> args[ 0 ] + "." + args[ 1 ], 2 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> cache.disposeComputedValue( "a" ) );

    assertEquals( exception.getMessage(),
                  "Arez-0163: MemoizeCache.disposeComputedValue called with 1 argument(s) but expected 2 argument(s)." );
  }

  @Test
  public void disposeComputedValue_noComputedValueCachedForArgs()
  {
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, ValueUtil.randomString(), args -> args[ 0 ] + "." + args[ 1 ], 1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> cache.disposeComputedValue( "a" ) );

    assertEquals( exception.getMessage(),
                  "Arez-0193: MemoizeCache.disposeComputedValue called with args [a] but unable to locate corresponding ComputedValue." );
  }

  @Test
  public void get_passedBadArgCounts()
  {
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, ValueUtil.randomString(), args -> args[ 0 ] + "." + args[ 1 ], 2 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> cache.get( "a" ) );

    assertEquals( exception.getMessage(),
                  "Arez-0162: MemoizeCache.getComputedValue called with 1 arguments but expected 2 arguments." );
  }

  @Test
  public void get_invokedOnDisposed()
  {
    final MemoizeCache<String> cache =
      new MemoizeCache<>( null, null, "X", args -> args[ 0 ] + "." + args[ 1 ], 2 );

    cache.dispose();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> cache.get( "a", "b" ) );

    assertEquals( exception.getMessage(), "Arez-0161: MemoizeCache named 'X' had get() invoked when disposed." );
  }

  @Test
  public void constructorPassedName_whenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new MemoizeCache<>( null, null, "X", args -> args[ 0 ], 1 ) );

    assertEquals( exception.getMessage(),
                  "Arez-0159: MemoizeCache passed a name 'X' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void constructorPassedBadArgCount()
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new MemoizeCache<>( null, null, "X", args -> args[ 0 ], 0 ) );

    assertEquals( exception.getMessage(),
                  "Arez-0160: MemoizeCache constructed with invalid argCount: 0. Expected positive value." );
  }

  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();
    ArezTestUtil.resetState();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new MemoizeCache<>( Arez.context(), null, null, args -> args[ 0 ], 1 ) );

    assertEquals( exception.getMessage(),
                  "Arez-174: MemoizeCache passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void constructorPassedBadFlags()
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new MemoizeCache<>( null, null, "X", args -> args[ 0 ], 1, Flags.KEEPALIVE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0211: MemoizeCache passed unsupported flags. Unsupported bits: " + Flags.KEEPALIVE );
  }
}
