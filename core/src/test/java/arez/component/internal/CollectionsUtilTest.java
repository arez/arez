package arez.component.internal;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.ArrayList;
import java.util.Collection;
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

public final class CollectionsUtilTest
  extends AbstractTest
{
  @Test
  public void wrap_when_areRepositoryResultsModifiable_isTrue()
  {
    final ArrayList<MyEntity> input = new ArrayList<>();
    final MyEntity entity = new MyEntity();
    input.add( entity );
    final List<MyEntity> output = CollectionsUtil.wrap( input );

    assertFalse( output == input );

    assertUnmodifiable( output, MyEntity::new );

    assertEquals( output.size(), 1 );
    assertEquals( output.get( 0 ), entity );
  }

  @Test
  public void wrap_List_when_areRepositoryResultsModifiable_isFalse()
  {
    final List<MyEntity> input = new ArrayList<>();
    final MyEntity entity = new MyEntity();
    input.add( entity );
    ArezTestUtil.makeCollectionPropertiesModifiable();
    final Collection<MyEntity> output = CollectionsUtil.wrap( input );

    assertCollectionModifiableVariant( input, entity, output );
  }

  @Test
  public void wrap_Set_when_areRepositoryResultsModifiable_isFalse()
  {
    final Set<MyEntity> input = new HashSet<>();
    final MyEntity entity = new MyEntity();
    input.add( entity );
    ArezTestUtil.makeCollectionPropertiesModifiable();
    final Collection<MyEntity> output = CollectionsUtil.wrap( input );

    assertCollectionModifiableVariant( input, entity, output );
  }

  @Test
  public void wrap_Collection_when_areRepositoryResultsModifiable_isFalse()
  {
    final Collection<MyEntity> input = new HashSet<>();
    final MyEntity entity = new MyEntity();
    input.add( entity );
    ArezTestUtil.makeCollectionPropertiesModifiable();
    final Collection<MyEntity> output = CollectionsUtil.wrap( input );

    assertCollectionModifiableVariant( input, entity, output );
  }

  @Test
  public void wrap_Map_when_areRepositoryResultsModifiable_isFalse()
  {
    final Map<String, MyEntity> input = new HashMap<>();
    final MyEntity entity = new MyEntity();
    input.put( ValueUtil.randomString(), entity );
    ArezTestUtil.makeCollectionPropertiesModifiable();
    final Map<String, MyEntity> output = CollectionsUtil.wrap( input );

    //noinspection SimplifiableAssertion
    assertTrue( output == input );

    assertEquals( output.size(), 1 );
    assertEquals( output.values().iterator().next(), entity );
  }

  private void assertCollectionModifiableVariant( @Nonnull final Collection<MyEntity> input,
                                                  @Nonnull final MyEntity entity,
                                                  @Nonnull final Collection<MyEntity> output )
  {
    //noinspection SimplifiableAssertion
    assertTrue( output == input );

    assertEquals( output.size(), 1 );
    assertEquals( output.iterator().next(), entity );
  }

  @Test
  public void asList()
  {
    final ArrayList<MyEntity> input = new ArrayList<>();
    final MyEntity entity = new MyEntity();
    input.add( entity );
    final List<MyEntity> output = CollectionsUtil.asList( input.stream() );

    assertUnmodifiable( output, MyEntity::new );

    assertEquals( output.size(), 1 );
    assertEquals( output.get( 0 ), entity );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> list, @Nonnull final Supplier<T> creator )
  {
    assertNotNull( list.iterator().next() );
    assertThrows( UnsupportedOperationException.class, () -> list.add( creator.get() ) );
  }

  static class MyEntity
  {
    MyEntity()
    {
    }
  }
}
