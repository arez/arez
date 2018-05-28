package arez.integration.collections;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.integration.AbstractIntegrationTest;
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

@SuppressWarnings( "Duplicates" )
public class UnmodifiableComputedCollectionsIntegrationTest
  extends AbstractIntegrationTest
{
  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Model1 m1 = Model1.create();

    context.safeAction( () -> assertUnmodifiable( m1.calcCollection(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnmodifiable( m1.calcSet(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnmodifiable( m1.calcList(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnsupportedOperation( () -> m1.calcMap().put( ValueUtil.randomString(),
                                                                                  ValueUtil.randomString() ) ) );

    context.safeAction( () -> assertNull( m1.calcCollection2() ) );
    context.safeAction( () -> assertNull( m1.calcSet2() ) );
    context.safeAction( () -> assertNull( m1.calcList2() ) );
    context.safeAction( () -> assertNull( m1.calcMap2() ) );

    // Reference identity should be guaranteed
    context.safeAction( () -> assertTrue( m1.calcCollection() == m1.calcCollection() ) );
    context.safeAction( () -> assertTrue( m1.calcSet() == m1.calcSet() ) );
    context.safeAction( () -> assertTrue( m1.calcList() == m1.calcList() ) );
    context.safeAction( () -> assertTrue( m1.calcMap() == m1.calcMap() ) );

    context.safeAction( () -> assertTrue( m1.calcCollection2() == m1.calcCollection2() ) );
    context.safeAction( () -> assertTrue( m1.calcSet2() == m1.calcSet2() ) );
    context.safeAction( () -> assertTrue( m1.calcList2() == m1.calcList2() ) );
    context.safeAction( () -> assertTrue( m1.calcMap2() == m1.calcMap2() ) );

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

    context.safeAction( () -> m1.setCollection( collection ) );
    context.safeAction( () -> m1.setSet( set ) );
    context.safeAction( () -> m1.setList( list ) );
    context.safeAction( () -> m1.setMap( map ) );

    context.safeAction( () -> m1.setCollection2( collection2 ) );
    context.safeAction( () -> m1.setSet2( set2 ) );
    context.safeAction( () -> m1.setList2( list2 ) );
    context.safeAction( () -> m1.setMap2( map2 ) );

    context.safeAction( () -> assertUnmodifiable( m1.calcCollection(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnmodifiable( m1.calcSet(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnmodifiable( m1.calcList(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnsupportedOperation( () -> m1.calcMap().put( ValueUtil.randomString(),
                                                                                  ValueUtil.randomString() ) ) );
    context.safeAction( () -> assertUnmodifiable( m1.calcCollection2(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnmodifiable( m1.calcSet2(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnmodifiable( m1.calcList2(), ValueUtil::randomString ) );
    context.safeAction( () -> assertUnsupportedOperation( () -> m1.calcMap2().put( ValueUtil.randomString(),
                                                                                   ValueUtil.randomString() ) ) );

    // Assert Collections contain expected
    context.safeAction( () -> assertTrue( m1.calcCollection().contains( collection.iterator().next() ) ) );
    context.safeAction( () -> assertTrue( m1.calcSet().contains( set.iterator().next() ) ) );
    context.safeAction( () -> assertTrue( m1.calcList().contains( list.iterator().next() ) ) );
    context.safeAction( () -> assertTrue( m1.calcMap().containsKey( map.keySet().iterator().next() ) ) );

    context.safeAction( () -> assertTrue( m1.calcCollection2().contains( collection2.iterator().next() ) ) );
    context.safeAction( () -> assertTrue( m1.calcSet2().contains( set2.iterator().next() ) ) );
    context.safeAction( () -> assertTrue( m1.calcList2().contains( list2.iterator().next() ) ) );
    context.safeAction( () -> assertTrue( m1.calcMap2().containsKey( map2.keySet().iterator().next() ) ) );

    // Reference identity should be guaranteed
    context.safeAction( () -> assertTrue( m1.calcCollection() == m1.calcCollection() ) );
    context.safeAction( () -> assertTrue( m1.calcSet() == m1.calcSet() ) );
    context.safeAction( () -> assertTrue( m1.calcList() == m1.calcList() ) );
    context.safeAction( () -> assertTrue( m1.calcMap() == m1.calcMap() ) );

    context.safeAction( () -> assertTrue( m1.calcCollection2() == m1.calcCollection2() ) );
    context.safeAction( () -> assertTrue( m1.calcSet2() == m1.calcSet2() ) );
    context.safeAction( () -> assertTrue( m1.calcList2() == m1.calcList2() ) );
    context.safeAction( () -> assertTrue( m1.calcMap2() == m1.calcMap2() ) );
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
      return new UnmodifiableComputedCollectionsIntegrationTest_Arez_Model1();
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


    @Computed
    @Nonnull
    Collection<String> calcCollection()
    {
      return getCollection();
    }

    @Computed
    @Nonnull
    Set<String> calcSet()
    {
      return getSet();
    }

    @Computed
    @Nonnull
    List<String> calcList()
    {
      return getList();
    }

    @Computed
    @Nonnull
    Map<String, String> calcMap()
    {
      return getMap();
    }

    @Computed
    @Nullable
    Collection<String> calcCollection2()
    {
      return getCollection2();
    }

    @Computed
    @Nullable
    Set<String> calcSet2()
    {
      return getSet2();
    }

    @Computed
    @Nullable
    List<String> calcList2()
    {
      return getList2();
    }

    @Computed
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
