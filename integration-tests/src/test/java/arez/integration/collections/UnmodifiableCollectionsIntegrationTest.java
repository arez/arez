package arez.integration.collections;

import arez.ArezTestUtil;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "SimplifiableAssertion" )
public final class UnmodifiableCollectionsIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final Model1 m1 = Model1.create();

    safeAction( () -> assertUnmodifiable( m1.getNames(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.getDates(), Date::new ) );
    safeAction( () -> assertUnmodifiable( m1.getTimes(), ValueUtil::randomLong ) );
    safeAction( () -> assertUnsupportedOperation( () -> m1.getPois().put( ValueUtil.randomString(),
                                                                          ValueUtil.randomString() ) ) );

    safeAction( () -> assertNull( m1.getNames2() ) );
    safeAction( () -> assertNull( m1.getDates2() ) );
    safeAction( () -> assertNull( m1.getTimes2() ) );
    safeAction( () -> assertNull( m1.getPois2() ) );

    // Reference identity should be guaranteed
    safeAction( () -> assertTrue( m1.getNames() == m1.getNames() ) );
    safeAction( () -> assertTrue( m1.getDates() == m1.getDates() ) );
    safeAction( () -> assertTrue( m1.getTimes() == m1.getTimes() ) );
    safeAction( () -> assertTrue( m1.getPois() == m1.getPois() ) );

    safeAction( () -> assertTrue( m1.getNames2() == m1.getNames2() ) );
    safeAction( () -> assertTrue( m1.getDates2() == m1.getDates2() ) );
    safeAction( () -> assertTrue( m1.getTimes2() == m1.getTimes2() ) );
    safeAction( () -> assertTrue( m1.getPois2() == m1.getPois2() ) );

    final HashSet<String> names = new HashSet<>( Arrays.asList( "1", "2", "3" ) );
    final HashSet<Date> dates = new HashSet<>( Collections.singletonList( new Date() ) );
    final List<Long> times = new ArrayList<>( Arrays.asList( 1L, 2L, 3L ) );
    final HashMap<String, String> pois = new HashMap<>();
    pois.put( ValueUtil.randomString(), ValueUtil.randomString() );
    final HashSet<String> names2 = new HashSet<>( Arrays.asList( "A", "B", "C" ) );
    final HashSet<Date> dates2 = new HashSet<>( Collections.singletonList( new Date() ) );
    final List<Long> times2 = new ArrayList<>( Arrays.asList( 1L, 2L, 3L ) );
    final HashMap<String, String> pois2 = new HashMap<>();
    pois2.put( ValueUtil.randomString(), ValueUtil.randomString() );

    safeAction( () -> m1.setNames( names ) );
    safeAction( () -> m1.setDates( dates ) );
    safeAction( () -> m1.setTimes( times ) );
    safeAction( () -> m1.setPois( pois ) );

    safeAction( () -> m1.setNames2( names2 ) );
    safeAction( () -> m1.setDates2( dates2 ) );
    safeAction( () -> m1.setTimes2( times2 ) );
    safeAction( () -> m1.setPois2( pois2 ) );

    safeAction( () -> assertUnmodifiable( m1.getNames(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.getDates(), Date::new ) );
    safeAction( () -> assertUnmodifiable( m1.getTimes(), ValueUtil::randomLong ) );
    safeAction( () -> assertUnsupportedOperation( () -> m1.getPois().put( ValueUtil.randomString(),
                                                                          ValueUtil.randomString() ) ) );
    safeAction( () -> assertUnmodifiable( m1.getNames2(), ValueUtil::randomString ) );
    safeAction( () -> assertUnmodifiable( m1.getDates2(), Date::new ) );
    safeAction( () -> assertUnmodifiable( m1.getTimes2(), ValueUtil::randomLong ) );
    safeAction( () -> assertUnsupportedOperation( () -> m1.getPois2().put( ValueUtil.randomString(),
                                                                           ValueUtil.randomString() ) ) );

    // Assert Collections contain expected
    safeAction( () -> assertTrue( m1.getNames().contains( names.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getDates().contains( dates.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getTimes().contains( times.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getPois().containsKey( pois.keySet().iterator().next() ) ) );

    safeAction( () -> assertTrue( m1.getNames2().contains( names2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getDates2().contains( dates2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getTimes2().contains( times2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getPois2().containsKey( pois2.keySet().iterator().next() ) ) );

    // Reference identity should be guaranteed
    safeAction( () -> assertTrue( m1.getNames() == m1.getNames() ) );
    safeAction( () -> assertTrue( m1.getDates() == m1.getDates() ) );
    safeAction( () -> assertTrue( m1.getTimes() == m1.getTimes() ) );
    safeAction( () -> assertTrue( m1.getPois() == m1.getPois() ) );

    safeAction( () -> assertTrue( m1.getNames2() == m1.getNames2() ) );
    safeAction( () -> assertTrue( m1.getDates2() == m1.getDates2() ) );
    safeAction( () -> assertTrue( m1.getTimes2() == m1.getTimes2() ) );
    safeAction( () -> assertTrue( m1.getPois2() == m1.getPois2() ) );
  }

  @Test
  public void scenario_when_CollectionPropertiesModifiable()
  {
    ArezTestUtil.makeCollectionPropertiesModifiable();

    final Model1 m1 = Model1.create();

    safeAction( () -> assertNotNull( m1.getNames() ) );
    safeAction( () -> assertNotNull( m1.getDates() ) );
    safeAction( () -> assertNotNull( m1.getTimes() ) );
    safeAction( () -> assertNotNull( m1.getPois() ) );

    safeAction( () -> assertNull( m1.getNames2() ) );
    safeAction( () -> assertNull( m1.getDates2() ) );
    safeAction( () -> assertNull( m1.getTimes2() ) );
    safeAction( () -> assertNull( m1.getPois2() ) );

    // Reference identity should be guaranteed
    safeAction( () -> assertTrue( m1.getNames() == m1.getNames() ) );
    safeAction( () -> assertTrue( m1.getDates() == m1.getDates() ) );
    safeAction( () -> assertTrue( m1.getTimes() == m1.getTimes() ) );
    safeAction( () -> assertTrue( m1.getPois() == m1.getPois() ) );

    safeAction( () -> assertTrue( m1.getNames2() == m1.getNames2() ) );
    safeAction( () -> assertTrue( m1.getDates2() == m1.getDates2() ) );
    safeAction( () -> assertTrue( m1.getTimes2() == m1.getTimes2() ) );
    safeAction( () -> assertTrue( m1.getPois2() == m1.getPois2() ) );

    final HashSet<String> names = new HashSet<>( Arrays.asList( "1", "2", "3" ) );
    final HashSet<Date> dates = new HashSet<>( Collections.singletonList( new Date() ) );
    final List<Long> times = new ArrayList<>( Arrays.asList( 1L, 2L, 3L ) );
    final HashMap<String, String> pois = new HashMap<>();
    pois.put( ValueUtil.randomString(), ValueUtil.randomString() );
    final HashSet<String> names2 = new HashSet<>( Arrays.asList( "A", "B", "C" ) );
    final HashSet<Date> dates2 = new HashSet<>( Collections.singletonList( new Date() ) );
    final List<Long> times2 = new ArrayList<>( Arrays.asList( 1L, 2L, 3L ) );
    final HashMap<String, String> pois2 = new HashMap<>();
    pois2.put( ValueUtil.randomString(), ValueUtil.randomString() );

    safeAction( () -> m1.setNames( names ) );
    safeAction( () -> m1.setDates( dates ) );
    safeAction( () -> m1.setTimes( times ) );
    safeAction( () -> m1.setPois( pois ) );

    safeAction( () -> m1.setNames2( names2 ) );
    safeAction( () -> m1.setDates2( dates2 ) );
    safeAction( () -> m1.setTimes2( times2 ) );
    safeAction( () -> m1.setPois2( pois2 ) );

    // assert collections all non null now
    safeAction( () -> assertNotNull( m1.getNames() ) );
    safeAction( () -> assertNotNull( m1.getDates() ) );
    safeAction( () -> assertNotNull( m1.getTimes() ) );
    safeAction( () -> assertNotNull( m1.getPois() ) );

    safeAction( () -> assertNotNull( m1.getNames2() ) );
    safeAction( () -> assertNotNull( m1.getDates2() ) );
    safeAction( () -> assertNotNull( m1.getTimes2() ) );
    safeAction( () -> assertNotNull( m1.getPois2() ) );

    // Assert Collections contain expected
    safeAction( () -> assertTrue( m1.getNames().contains( names.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getDates().contains( dates.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getTimes().contains( times.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getPois().containsKey( pois.keySet().iterator().next() ) ) );

    safeAction( () -> assertTrue( m1.getNames2().contains( names2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getDates2().contains( dates2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getTimes2().contains( times2.iterator().next() ) ) );
    safeAction( () -> assertTrue( m1.getPois2().containsKey( pois2.keySet().iterator().next() ) ) );

    // Reference identity should be guaranteed
    safeAction( () -> assertTrue( m1.getNames() == m1.getNames() ) );
    safeAction( () -> assertTrue( m1.getDates() == m1.getDates() ) );
    safeAction( () -> assertTrue( m1.getTimes() == m1.getTimes() ) );
    safeAction( () -> assertTrue( m1.getPois() == m1.getPois() ) );

    safeAction( () -> assertTrue( m1.getNames2() == m1.getNames2() ) );
    safeAction( () -> assertTrue( m1.getDates2() == m1.getDates2() ) );
    safeAction( () -> assertTrue( m1.getTimes2() == m1.getTimes2() ) );
    safeAction( () -> assertTrue( m1.getPois2() == m1.getPois2() ) );

    // Reset to Null
    safeAction( () -> m1.setNames2( null ) );
    safeAction( () -> m1.setDates2( null ) );
    safeAction( () -> m1.setTimes2( null ) );
    safeAction( () -> m1.setPois2( null ) );

    // Verify null
    safeAction( () -> assertNull( m1.getNames2() ) );
    safeAction( () -> assertNull( m1.getDates2() ) );
    safeAction( () -> assertNull( m1.getTimes2() ) );
    safeAction( () -> assertNull( m1.getPois2() ) );
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
      return new UnmodifiableCollectionsIntegrationTest_Arez_Model1( new HashSet<>(),
                                                                     new HashSet<>(),
                                                                     new ArrayList<>(),
                                                                     new HashMap<>() );
    }

    @Observable
    @Nonnull
    abstract Collection<String> getNames();

    abstract void setNames( @Nonnull Collection<String> value );

    @Observable
    @Nonnull
    abstract Set<Date> getDates();

    abstract void setDates( @Nonnull Set<Date> value );

    @Observable
    @Nonnull
    abstract List<Long> getTimes();

    abstract void setTimes( @Nonnull List<Long> value );

    @Observable
    @Nonnull
    abstract Map<String, String> getPois();

    abstract void setPois( @Nonnull Map<String, String> value );

    @Observable
    abstract Collection<String> getNames2();

    abstract void setNames2( Collection<String> value );

    @Observable
    abstract Set<Date> getDates2();

    abstract void setDates2( Set<Date> value );

    @Observable
    abstract List<Long> getTimes2();

    abstract void setTimes2( List<Long> value );

    @Observable
    abstract Map<String, String> getPois2();

    abstract void setPois2( Map<String, String> value );
  }
}
