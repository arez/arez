package arez.component;

import arez.ArezTestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RepositoryUtilTest
  extends AbstractArezComponentTest
{
  @Test
  public void wrap_when_areRepositoryResultsModifiable_isTrue()
  {
    final ArrayList<MyEntity> input = new ArrayList<>();
    final MyEntity entity = new MyEntity();
    input.add( entity );
    final List<MyEntity> output = RepositoryUtil.toResults( input );

    assertFalse( output == input );

    assertListUnmodifiable( output, MyEntity::new );

    assertEquals( output.size(), 1 );
    assertEquals( output.get( 0 ), entity );
  }

  @Test
  public void wrap_when_areRepositoryResultsModifiable_isFalse()
  {
    ArezTestUtil.makeRepositoryResultsModifiable();

    final ArrayList<MyEntity> input = new ArrayList<>();
    final MyEntity entity = new MyEntity();
    input.add( entity );
    final List<MyEntity> output = RepositoryUtil.toResults( input );

    assertTrue( output == input );

    assertEquals( output.size(), 1 );
    assertEquals( output.get( 0 ), entity );
  }

  @Test
  public void asList()
  {
    final ArrayList<MyEntity> input = new ArrayList<>();
    final MyEntity entity = new MyEntity();
    input.add( entity );
    final List<MyEntity> output = RepositoryUtil.asList( input.stream() );

    assertListUnmodifiable( output, MyEntity::new );

    assertEquals( output.size(), 1 );
    assertEquals( output.get( 0 ), entity );
  }

  private <T> void assertListUnmodifiable( @Nonnull final List<T> list, @Nonnull final Supplier<T> creator )
  {
    assertNotNull( list.get( 0 ) );
    assertThrows( UnsupportedOperationException.class, () -> list.add( creator.get() ) );
  }

  static class MyEntity
  {
    MyEntity()
    {
    }
  }
}
