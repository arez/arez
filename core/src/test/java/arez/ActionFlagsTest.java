package arez;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ActionFlagsTest
  extends AbstractArezTest
{
  @Test
  public void isVerifyActionRuleValid()
  {
    assertTrue( ActionFlags.isVerifyActionRuleValid( ActionFlags.VERIFY_ACTION_REQUIRED ) );
    assertTrue( ActionFlags.isVerifyActionRuleValid( ActionFlags.NO_VERIFY_ACTION_REQUIRED ) );
    assertFalse( ActionFlags.isVerifyActionRuleValid( 0 ) );
    assertFalse( ActionFlags.isVerifyActionRuleValid( ActionFlags.VERIFY_ACTION_REQUIRED |
                                                      ActionFlags.NO_VERIFY_ACTION_REQUIRED ) );
  }

  @Test
  public void verifyActionRule()
  {
    assertEquals( ActionFlags.verifyActionRule( ActionFlags.VERIFY_ACTION_REQUIRED ), 0 );
    assertEquals( ActionFlags.verifyActionRule( ActionFlags.NO_VERIFY_ACTION_REQUIRED ), 0 );
    assertEquals( ActionFlags.verifyActionRule( 0 ), ActionFlags.VERIFY_ACTION_REQUIRED );
    assertEquals( ActionFlags.verifyActionRule( ActionFlags.REQUIRE_NEW_TRANSACTION ),
                  ActionFlags.VERIFY_ACTION_REQUIRED );
  }

  @Test
  public void flagsNoOverlap()
    throws Exception
  {
    final HashMap<String, Integer> flags = extractFlags();
    final ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>( flags.entrySet() );
    final int size = entries.size();
    for ( int i = 0; i < size; i++ )
    {
      final Map.Entry<String, Integer> entry = entries.get( i );
      final String name = entry.getKey();

      for ( int j = i + 1; j < size; j++ )
      {
        final Map.Entry<String, Integer> innerEntry = entries.get( j );
        final String innerName = innerEntry.getKey();
        final int value = entry.getValue();
        final int innerValue = innerEntry.getValue();
        if ( ( innerValue & value ) != 0 )
        {
          fail( "Flags in class " + ActionFlags.class.getName() + " overlap. Flag named " + name +
                " and flag named " + innerName + " have the values " + value + " and " + innerValue +
                " which overlap bits. These flags are not expected to overlap." );
        }
      }
    }
  }

  @Test
  public void flagsAreCoveredByMasks()
    throws Exception
  {
    for ( final Map.Entry<String, Integer> entry : new ArrayList<>( extractFlags().entrySet() ) )
    {
      final int value = entry.getValue();
      if ( ( ( ActionFlags.CONFIG_FLAGS_MASK ) & value ) != value )
      {
        fail( "Flag named " + entry.getKey() + " in class " + ActionFlags.class.getName() + " is not within " +
              "expected configuration mask. Update mask or configuration value." );
      }
    }
  }

  @Nonnull
  private HashMap<String, Integer> extractFlags()
    throws IllegalAccessException
  {
    final HashMap<String, Integer> flags = new HashMap<>();
    for ( final Field field : ActionFlags.class.getDeclaredFields() )
    {
      final String name = field.getName();
      if ( !name.endsWith( "_MASK" ) &&
           !name.endsWith( "_SHIFT" ) &&
           !Modifier.isPrivate( field.getModifiers() ) &&
           !field.isSynthetic() )
      {
        final int value = (Integer) field.get( null );
        flags.put( name, value );
      }
    }
    return flags;
  }
}
