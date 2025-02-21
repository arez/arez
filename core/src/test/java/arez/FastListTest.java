package arez;

import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class FastListTest
{
  @Test
  public void basicOperation()
  {
    final FastList<Object> list = new FastList<>();
    assertEquals( list.size(), 0 );
    assertTrue( list.isEmpty() );
    assertFalse( list.contains( "1" ) );

    list.add( "1" );
    list.add( "2" );
    list.add( "3" );

    assertEquals( list.size(), 3 );
    assertFalse( list.isEmpty() );

    assertToString( list, "123" );

    assertTrue( list.contains( "1" ) );
    assertTrue( list.contains( "2" ) );
    assertTrue( list.contains( "3" ) );
    assertFalse( list.contains( "4" ) );

    assertEquals( list.get( 0 ), "1" );
    assertEquals( list.get( 1 ), "2" );
    assertEquals( list.get( 2 ), "3" );

    list.add( "4" );

    assertEquals( list.size(), 4 );
    assertFalse( list.isEmpty() );

    assertToString( list, "1234" );

    assertEquals( list.get( 0 ), "1" );
    assertEquals( list.get( 1 ), "2" );
    assertEquals( list.get( 2 ), "3" );
    assertEquals( list.get( 3 ), "4" );

    list.remove( "2" );

    assertEquals( list.size(), 3 );
    assertFalse( list.isEmpty() );

    assertToString( list, "134" );

    assertEquals( list.get( 0 ), "1" );
    assertEquals( list.get( 1 ), "3" );
    assertEquals( list.get( 2 ), "4" );

    list.remove( "2" );

    assertEquals( list.size(), 3 );
    assertFalse( list.isEmpty() );

    assertToString( list, "134" );

    assertEquals( list.get( 0 ), "1" );
    assertEquals( list.get( 1 ), "3" );
    assertEquals( list.get( 2 ), "4" );

    list.add( "5" );
    list.add( "5" );
    list.add( "5" );

    assertEquals( list.size(), 4 );
    assertFalse( list.isEmpty() );

    assertToString( list, "1345" );

    assertEquals( list.get( 0 ), "1" );
    assertEquals( list.get( 1 ), "3" );
    assertEquals( list.get( 2 ), "4" );
    assertEquals( list.get( 3 ), "5" );

    list.set( 0, "5" );

    assertEquals( list.size(), 3 );
    assertFalse( list.isEmpty() );

    assertToString( list, "534" );

    assertEquals( list.get( 0 ), "5" );
    assertEquals( list.get( 1 ), "3" );
    assertEquals( list.get( 2 ), "4" );

    list.set( 0, "7" );

    assertEquals( list.size(), 3 );
    assertFalse( list.isEmpty() );

    assertToString( list, "734" );

    assertEquals( list.get( 0 ), "7" );
    assertEquals( list.get( 1 ), "3" );
    assertEquals( list.get( 2 ), "4" );

    list.set( 2, "8" );

    assertEquals( list.size(), 3 );
    assertFalse( list.isEmpty() );

    assertToString( list, "738" );

    assertEquals( list.get( 0 ), "7" );
    assertEquals( list.get( 1 ), "3" );
    assertEquals( list.get( 2 ), "8" );

    list.remove( 1 );

    assertEquals( list.size(), 2 );
    assertFalse( list.isEmpty() );

    assertToString( list, "78" );

    assertEquals( list.get( 0 ), "7" );
    assertEquals( list.get( 1 ), "8" );

    list.clear();

    assertEquals( list.size(), 0 );
    assertTrue( list.isEmpty() );

    assertToString( list, "" );
  }

  private static void assertToString( @Nonnull final FastList<Object> list, @Nonnull final String expected )
  {
    {
      final var sb = new StringBuilder();
      list.forEach( sb::append );
      assertEquals( sb.toString(), expected, "forEach collection of values" );
    }
    {
      final var sb = new StringBuilder();
      list.stream().forEach( sb::append );
      assertEquals( sb.toString(), expected, "stream() collection of values" );
    }
  }
}
