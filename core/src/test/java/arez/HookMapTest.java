package arez;

import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class HookMapTest
  extends AbstractTest
{
  @Test
  public void empty()
  {
    final HookMap map = new HookMap();

    assertEquals( map.size(), 0 );
    assertTrue( map.isEmpty() );
    assertFalse( map.containsKey( "X" ) );
    assertNull( map.get( "X" ) );
  }

  @Test
  public void put_appendsInInsertionOrder()
  {
    final HookMap map = new HookMap();
    final Hook hook1 = hook();
    final Hook hook2 = hook();
    final Hook hook3 = hook();

    map.put( "3", hook1 );
    map.put( "2", hook2 );
    map.put( "1", hook3 );

    assertEquals( map.size(), 3 );
    assertFalse( map.isEmpty() );
    assertSame( map.get( "3" ), hook1 );
    assertSame( map.get( "2" ), hook2 );
    assertSame( map.get( "1" ), hook3 );
    assertOrder( map, "3,2,1" );
  }

  @Test
  public void put_replacesExistingValueWithoutReordering()
  {
    final HookMap map = new HookMap();
    final Hook hook1 = hook();
    final Hook hook2 = hook();
    final Hook hook3 = hook();

    map.put( "A", hook1 );
    map.put( "B", hook2 );
    map.put( "A", hook3 );

    assertEquals( map.size(), 2 );
    assertSame( map.get( "A" ), hook3 );
    assertSame( map.get( "B" ), hook2 );
    assertOrder( map, "A,B" );
  }

  @Test
  public void clear_andReuse()
  {
    final HookMap map = new HookMap();
    map.put( "A", hook() );
    map.put( "B", hook() );

    map.clear();

    assertEquals( map.size(), 0 );
    assertTrue( map.isEmpty() );
    assertFalse( map.containsKey( "A" ) );
    assertNull( map.get( "A" ) );

    final Hook hook = hook();
    map.put( "C", hook );

    assertEquals( map.size(), 1 );
    assertSame( map.get( "C" ), hook );
    assertOrder( map, "C" );
  }

  @Test
  public void growthPreservesOrder()
  {
    final HookMap map = new HookMap();

    for ( int i = 0; i < 6; i++ )
    {
      map.put( "K" + i, hook() );
    }

    assertEquals( map.size(), 6 );
    assertOrder( map, "K0,K1,K2,K3,K4,K5" );
    assertTrue( map.containsKey( "K5" ) );
    assertNull( map.get( "Missing" ) );
  }

  @Nonnull
  private static Hook hook()
  {
    return new Hook( new NoopProcedure(), new NoopProcedure() );
  }

  private static void assertOrder( @Nonnull final HookMap map, @Nonnull final String expected )
  {
    final StringBuilder sb = new StringBuilder();
    for ( int i = 0; i < map.size(); i++ )
    {
      if ( 0 != i )
      {
        sb.append( ',' );
      }
      sb.append( map.keyAt( i ) );
    }
    assertEquals( sb.toString(), expected );
  }
}
