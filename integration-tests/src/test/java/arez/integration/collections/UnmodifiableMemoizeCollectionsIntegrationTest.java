package arez.integration.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "SimplifiableAssertion" )
public final class UnmodifiableMemoizeCollectionsIntegrationTest
  extends AbstractArezIntegrationTest
{
  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void scenario()
  {
    final Model1 m1 = Model1.create();

    safeAction( () -> assertUnmodifiable( m1.calcCollection(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.calcSet(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.calcList(), ValueUtil::randomString ) );
    safeAction( () -> assertUnsupportedOperation( () -> m1.calcMap().put( ValueUtil.randomString(),
                                                                          ValueUtil.randomString() ) ) );

    safeAction( () -> assertNull( m1.calcCollection2() ) );
    safeAction( () -> assertNull( m1.calcSet2() ) );
    safeAction( () -> assertNull( m1.calcList2() ) );
    safeAction( () -> assertNull( m1.calcMap2() ) );

    // Reference identity should be guaranteed
    safeAction( () -> assertTrue( m1.calcCollection() == m1.calcCollection() ) );
    safeAction( () -> assertTrue( m1.calcSet() == m1.calcSet() ) );
    safeAction( () -> assertTrue( m1.calcList() == m1.calcList() ) );
    safeAction( () -> assertTrue( m1.calcMap() == m1.calcMap() ) );

    safeAction( () -> assertTrue( m1.calcCollection2() == m1.calcCollection2() ) );
    safeAction( () -> assertTrue( m1.calcSet2() == m1.calcSet2() ) );
    safeAction( () -> assertTrue( m1.calcList2() == m1.calcList2() ) );
    safeAction( () -> assertTrue( m1.calcMap2() == m1.calcMap2() ) );

    final Collection<String> collection = new HashSet<>( Arrays.asList( "1", "2", "3" ) );
    final Set<String> set = new HashSet<>( Arrays.asList( "1", "2", "3" ) );
    final List<String> list = new ArrayList<>( Arrays.asList( "1", "2", "3" ) );
    final HashMap<String, String> map = new HashMap<>();
    map.put( ValueUtil.randomString(), ValueUtil.randomString() );

    final Collection<String> collection2 = new HashSet<>( Arrays.asList( "1", "2", "3" ) );
    final Set<String> set2 = new HashSet<>( Arrays.asList( "1", "2", "3" ) );
    final List<String> list2 = new ArrayList<>( Arrays.asList( "1", "2", "3" ) );
    final HashMap<String, String> map2 = new HashMap<>();
    map2.put( ValueUtil.randomString(), ValueUtil.randomString() );

    safeAction( () -> m1.setCollection( collection ) );
    safeAction( () -> m1.setSet( set ) );
    safeAction( () -> m1.setList( list ) );
    safeAction( () -> m1.setMap( map ) );

    safeAction( () -> m1.setCollection2( collection2 ) );
    safeAction( () -> m1.setSet2( set2 ) );
    safeAction( () -> m1.setList2( list2 ) );
    safeAction( () -> m1.setMap2( map2 ) );

    safeAction( () -> assertUnmodifiable( m1.calcCollection(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.calcSet(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.calcList(), ValueUtil::randomString ) );
    safeAction( () -> assertUnsupportedOperation( () -> m1.calcMap().put( ValueUtil.randomString(),
                                                                          ValueUtil.randomString() ) ) );
    safeAction( () -> assertUnmodifiable( m1.calcCollection2(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.calcSet2(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.calcList2(), ValueUtil::randomString ) );
    safeAction( () -> assertUnsupportedOperation( () -> m1.calcMap2().put( ValueUtil.randomString(),
                                                                           ValueUtil.randomString() ) ) );

    // Assert Collections contain expected
    safeAction( () -> assertTrue( m1.calcCollection().contains( collection.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.calcSet().contains( set.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.calcList().contains( list.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.calcMap().containsKey( map.keySet().iterator().next() ) ) );

    safeAction( () -> assertTrue( m1.calcCollection2().contains( collection2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.calcSet2().contains( set2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.calcList2().contains( list2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.calcMap2().containsKey( map2.keySet().iterator().next() ) ) );

    // Reference identity should be guaranteed
    safeAction( () -> assertTrue( m1.calcCollection() == m1.calcCollection() ) );
    safeAction( () -> assertTrue( m1.calcSet() == m1.calcSet() ) );
    safeAction( () -> assertTrue( m1.calcList() == m1.calcList() ) );
    safeAction( () -> assertTrue( m1.calcMap() == m1.calcMap() ) );

    safeAction( () -> assertTrue( m1.calcCollection2() == m1.calcCollection2() ) );
    safeAction( () -> assertTrue( m1.calcSet2() == m1.calcSet2() ) );
    safeAction( () -> assertTrue( m1.calcList2() == m1.calcList2() ) );
    safeAction( () -> assertTrue( m1.calcMap2() == m1.calcMap2() ) );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection, @Nonnull final Supplier<T> creator )
  {
    assertUnsupportedOperation( () -> collection.add( creator.get() ) );
  }

  private void assertUnsupportedOperation( final ThrowingRunnable runnable )
  {
    assertThrows( UnsupportedOperationException.class, runnable );
  }

  @SuppressWarnings( "SameParameterValue" )
  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    static Model1 create()
    {
      return new UnmodifiableMemoizeCollectionsIntegrationTest_Arez_Model1();
    }

    @Nonnull
    private Collection<String> _collection = new HashSet<>();
    @Nonnull
    private Set<String> _set = new HashSet<>();
    @Nonnull
    private List<String> _list = new ArrayList<>();
    @Nonnull
    private Map<String, String> _map = new HashMap<>();
    @Nullable
    private Collection<String> _collection2;
    @Nullable
    private Set<String> _set2;
    @Nullable
    private List<String> _list2;
    @Nullable
    private Map<String, String> _map2;

    @Memoize
    @Nonnull
    Collection<String> calcCollection()
    {
      return getCollection();
    }

    @Memoize
    @Nonnull
    Set<String> calcSet()
    {
      return getSet();
    }

    @Memoize
    @Nonnull
    List<String> calcList()
    {
      return getList();
    }

    @Memoize
    @Nonnull
    Map<String, String> calcMap()
    {
      return getMap();
    }

    @Memoize
    @Nullable
    Collection<String> calcCollection2()
    {
      return getCollection2();
    }

    @Memoize
    @Nullable
    Set<String> calcSet2()
    {
      return getSet2();
    }

    @Memoize
    @Nullable
    List<String> calcList2()
    {
      return getList2();
    }

    @Memoize
    @Nullable
    Map<String, String> calcMap2()
    {
      return getMap2();
    }

    @Observable
    @Nonnull
    Collection<String> getCollection()
    {
      return _collection;
    }

    @Observable
    @Nonnull
    Set<String> getSet()
    {
      return _set;
    }

    @Observable
    @Nonnull
    List<String> getList()
    {
      return _list;
    }

    @Observable
    @Nonnull
    Map<String, String> getMap()
    {
      return _map;
    }

    @Observable
    @Nullable
    Collection<String> getCollection2()
    {
      return _collection2;
    }

    @Observable
    @Nullable
    Set<String> getSet2()
    {
      return _set2;
    }

    @Observable
    @Nullable
    List<String> getList2()
    {
      return _list2;
    }

    @Observable
    @Nullable
    Map<String, String> getMap2()
    {
      return _map2;
    }

    void setCollection( @Nonnull final Collection<String> collection )
    {
      _collection = collection;
    }

    void setSet( @Nonnull final Set<String> set )
    {
      _set = set;
    }

    void setList( @Nonnull final List<String> list )
    {
      _list = list;
    }

    void setMap( @Nonnull final Map<String, String> map )
    {
      _map = map;
    }

    void setCollection2( @Nullable final Collection<String> collection2 )
    {
      _collection2 = collection2;
    }

    void setSet2( @Nullable final Set<String> set2 )
    {
      _set2 = set2;
    }

    void setList2( @Nullable final List<String> list2 )
    {
      _list2 = list2;
    }

    void setMap2( @Nullable final Map<String, String> map2 )
    {
      _map2 = map2;
    }
  }
}
