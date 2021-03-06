package arez;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class FlagsTest
  extends AbstractTest
{
  @Test
  public void flagsWithTheSameNameHaveTheSameValue()
    throws Exception
  {
    // IntelliJ IDEA annotation @MagicConstant(flagsFromClass=Observer.Flags.class) will not generate an error
    // if ComputableValue.Flags.PRIORITY_LOW is passed rather than Observer.Flags.PRIORITY_LOW if
    // ComputableValue.Flags.PRIORITY_LOW is aliased (i.e. assigned) to Observer.Flags.PRIORITY_LOW. So we
    // ensure both variables are assigned the underlying value. However we have to then ensure that the values
    // stay consistent over time which is what this test is doing.
    final Map<Class<?>, Map<String, Integer>> valueMap = new HashMap<>();
    valueMap.put( Observer.Flags.class, extractFlags( Observer.Flags.class ) );
    valueMap.put( ComputableValue.Flags.class, extractFlags( ComputableValue.Flags.class ) );
    valueMap.put( Transaction.Flags.class, extractFlags( Transaction.Flags.class ) );
    valueMap.put( Task.Flags.class, extractFlags( Task.Flags.class ) );
    valueMap.put( ActionFlags.class, extractFlags( ActionFlags.class ) );

    for ( final Map.Entry<Class<?>, Map<String, Integer>> typeEntry : valueMap.entrySet() )
    {
      final Class<?> type = typeEntry.getKey();
      for ( final Map.Entry<String, Integer> flagEntry : typeEntry.getValue().entrySet() )
      {
        final String flag = flagEntry.getKey();
        if( flag.startsWith( "STATE_" ) || flag.endsWith( "_MASK" ) )
        {
          continue;
        }
        for ( final Map.Entry<Class<?>, Map<String, Integer>> te : valueMap.entrySet() )
        {
          final Class<?> otherType = te.getKey();
          if ( otherType != type )
          {
            final Integer value = te.getValue().get( flag );
            if ( null != value )
            {
              assertEquals( value,
                            flagEntry.getValue(),
                            "Expected flag named " + flag + " to have the same value in " +
                            type.getName() + "  and " + otherType.getName() + " flag classes." );
            }
          }
        }
      }
    }
  }

  @Test
  public void flagsAreUnique()
    throws Exception
  {
    flagsAreUnique( Observer.Flags.class );
    flagsAreUnique( ComputableValue.Flags.class );
    flagsAreUnique( Task.Flags.class );
    flagsAreUnique( ActionFlags.class );
  }

  private void flagsAreUnique( @Nonnull final Class<?> type )
    throws Exception
  {
    final HashMap<String, Integer> flags = extractFlags( type );
    final ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>( flags.entrySet() );
    final int size = entries.size();
    for ( int i = 0; i < size; i++ )
    {
      final Map.Entry<String, Integer> entry = entries.get( i );
      final String name = entry.getKey();
      final int value = entry.getValue();

      for ( int j = i + 1; j < size; j++ )
      {
        final Map.Entry<String, Integer> innerEntry = entries.get( j );
        final String innerName = innerEntry.getKey();
        final int innerValue = innerEntry.getValue();
        if ( innerValue == value )
        {
          fail( "Flags in class " + type.getName() + " are not unique. Flag named " + name +
                " and flag named " + innerName + " have the same value: " + value );
        }
      }
    }
  }

  @Test
  public void flagsNoOverlap()
    throws Exception
  {
    flagsNoOverlap( Observer.Flags.class );
    flagsNoOverlap( ComputableValue.Flags.class );
    flagsNoOverlap( Task.Flags.class );
    flagsNoOverlap( ActionFlags.class );
  }

  private void flagsNoOverlap( @Nonnull final Class<?> type )
    throws Exception
  {
    final ArrayList<String> exceptions = new ArrayList<>();
    exceptions.add( "PRIORITY_COUNT" );

    // Configs with these prefixes may overlap
    final ArrayList<String> exceptionPrefixes = new ArrayList<>();
    exceptionPrefixes.add( "PRIORITY_" );
    exceptionPrefixes.add( "STATE_" );

    final HashMap<String, Integer> flags = extractFlags( type );
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
        if ( !exceptions.contains( name ) &&
             !exceptions.contains( innerName ) &&
             exceptionPrefixes.stream().noneMatch( p -> name.startsWith( p ) && innerName.startsWith( p ) ) )
        {
          final int value = entry.getValue();
          final int innerValue = innerEntry.getValue();
          if ( ( innerValue & value ) != 0 )
          {
            fail( "Flags in class " + type.getName() + " overlap. Flag named " + name +
                  " and flag named " + innerName + " have the values " + value + " and " + innerValue +
                  " which overlap bits. These flags are not expected to overlap." );
          }
        }
      }
    }
  }

  @Test
  public void flagsAreCoveredByMasks()
    throws Exception
  {
    flagsAreCoveredByMasks( Observer.Flags.class,
                            Observer.Flags.CONFIG_FLAGS_MASK | Observer.Flags.RUNTIME_FLAGS_MASK );
    flagsAreCoveredByMasks( ComputableValue.Flags.class, ComputableValue.Flags.CONFIG_FLAGS_MASK );
    flagsAreCoveredByMasks( Task.Flags.class, Task.Flags.CONFIG_FLAGS_MASK | Task.Flags.RUNTIME_FLAGS_MASK );
    flagsAreCoveredByMasks( ActionFlags.class, ActionFlags.CONFIG_FLAGS_MASK );
  }

  private void flagsAreCoveredByMasks( @Nonnull final Class<?> type, final int mask )
    throws Exception
  {
    for ( final Map.Entry<String, Integer> entry : new ArrayList<>( extractFlags( type ).entrySet() ) )
    {
      final String name = entry.getKey();
      final int value = entry.getValue();
      if ( !"PRIORITY_COUNT".equals( name ) && ( mask & value ) != value )
      {
        fail( "Flag named " + name + " in class " + type.getName() + " is not within " +
              "expected configuration mask. Update mask or configuration value." );
      }
    }
  }

  @Nonnull
  private HashMap<String, Integer> extractFlags( @Nonnull final Class<?> type )
    throws IllegalAccessException
  {
    final HashMap<String, Integer> flags = new HashMap<>();
    for ( final Field field : type.getDeclaredFields() )
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
