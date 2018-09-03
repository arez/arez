package arez;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class OptionsTest
  extends AbstractArezTest
{
  @Test
  public void isPrioritySpecified()
    throws Exception
  {
    assertTrue( Options.isPrioritySpecified( Options.PRIORITY_HIGHEST ) );
    assertTrue( Options.isPrioritySpecified( Options.PRIORITY_HIGH ) );
    assertTrue( Options.isPrioritySpecified( Options.PRIORITY_NORMAL ) );
    assertTrue( Options.isPrioritySpecified( Options.PRIORITY_LOW ) );
    assertTrue( Options.isPrioritySpecified( Options.PRIORITY_LOWEST ) );
    assertFalse( Options.isPrioritySpecified( 0 ) );
    assertFalse( Options.isPrioritySpecified( Options.READ_WRITE ) );
  }

  @Test
  public void extractPriority()
    throws Exception
  {
    assertEquals( Options.extractPriority( Options.PRIORITY_HIGHEST | State.STATE_INACTIVE ), 0 );
    assertEquals( Options.extractPriority( Options.PRIORITY_HIGH | State.STATE_INACTIVE ), 1 );
    assertEquals( Options.extractPriority( Options.PRIORITY_NORMAL | State.STATE_INACTIVE ), 2 );
    assertEquals( Options.extractPriority( Options.PRIORITY_LOW | State.STATE_INACTIVE ), 3 );
    assertEquals( Options.extractPriority( Options.PRIORITY_LOWEST | State.STATE_INACTIVE ), 4 );
  }

  @Test
  public void isTransactionModeSpecified()
    throws Exception
  {
    assertTrue( Options.isTransactionModeSpecified( Options.READ_ONLY ) );
    assertTrue( Options.isTransactionModeSpecified( Options.READ_WRITE ) );
    assertFalse( Options.isTransactionModeSpecified( 0 ) );
    assertFalse( Options.isTransactionModeSpecified( Options.PRIORITY_LOWEST ) );
  }

  @Test
  public void optionsAreUnique()
    throws Exception
  {
    final HashMap<String, Integer> values = new HashMap<>();
    for ( final Field field : Options.class.getDeclaredFields() )
    {
      final String name = field.getName();
      if ( Modifier.isPublic( field.getModifiers() ) && !name.endsWith( "_MASK" ) && !name.endsWith( "_SHIFT" ) )
      {
        values.put( name, (Integer) field.get( null ) );
      }
    }
    for ( final Field field : State.class.getDeclaredFields() )
    {
      final String name = field.getName();
      if ( !Modifier.isPrivate( field.getModifiers() ) &&
           !field.isSynthetic() &&
           !name.endsWith( "_MASK" ) &&
           !name.endsWith( "_SHIFT" ) )
      {
        values.put( name, (Integer) field.get( null ) );
      }
    }
    final ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>( values.entrySet() );
    final int size = entries.size();
    for ( int i = 0; i < size; i++ )
    {
      final Map.Entry<String, Integer> outerEntry = entries.get( i );
      final int outerValue = outerEntry.getValue();

      if ( ( ( Options.OPTIONS_MASK | State.RUNTIME_CONFIG_MASK ) & outerValue ) != outerValue )
      {
        fail( "Constant " + outerEntry.getKey() + " in class " + Options.class.getName() + " is not within " +
              "expected configuration mask. Update mask or configuration value." );
      }

      for ( int j = i + 1; j < size; j++ )
      {
        final Map.Entry<String, Integer> innerEntry = entries.get( j );
        final int innerValue = innerEntry.getValue();
        if ( innerValue == outerValue )
        {
          fail( "Constants in class " + Options.class.getName() + " are not unique. Field " + outerEntry.getKey() +
                " and field " + innerEntry.getKey() + " have the same value: " + outerValue );
        }
      }
    }
  }
}
