package arez;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class FlagsTest
  extends AbstractArezTest
{
  @Test
  public void isActive()
    throws Exception
  {
    assertFalse( Flags.isActive( Flags.STATE_DISPOSED ) );
    assertFalse( Flags.isActive( Flags.STATE_DISPOSING ) );
    assertFalse( Flags.isActive( Flags.STATE_INACTIVE ) );
    assertTrue( Flags.isActive( Flags.STATE_UP_TO_DATE ) );
    assertTrue( Flags.isActive( Flags.STATE_POSSIBLY_STALE ) );
    assertTrue( Flags.isActive( Flags.STATE_STALE ) );
  }

  @Test
  public void isNotActive()
    throws Exception
  {
    assertTrue( Flags.isNotActive( Flags.STATE_DISPOSED ) );
    assertTrue( Flags.isNotActive( Flags.STATE_DISPOSING ) );
    assertTrue( Flags.isNotActive( Flags.STATE_INACTIVE ) );
    assertFalse( Flags.isNotActive( Flags.STATE_UP_TO_DATE ) );
    assertFalse( Flags.isNotActive( Flags.STATE_POSSIBLY_STALE ) );
    assertFalse( Flags.isNotActive( Flags.STATE_STALE ) );
  }

  @Test
  public void getState()
    throws Exception
  {
    assertEquals( Flags.getState( Flags.PRIORITY_NORMAL | Flags.STATE_DISPOSED ), Flags.STATE_DISPOSED );
    assertEquals( Flags.getState( Flags.PRIORITY_NORMAL | Flags.STATE_DISPOSING ), Flags.STATE_DISPOSING );
    assertEquals( Flags.getState( Flags.PRIORITY_NORMAL | Flags.STATE_INACTIVE ), Flags.STATE_INACTIVE );
    assertEquals( Flags.getState( Flags.PRIORITY_NORMAL | Flags.STATE_UP_TO_DATE ), Flags.STATE_UP_TO_DATE );
    assertEquals( Flags.getState( Flags.PRIORITY_NORMAL | Flags.STATE_POSSIBLY_STALE ), Flags.STATE_POSSIBLY_STALE );
    assertEquals( Flags.getState( Flags.PRIORITY_NORMAL | Flags.STATE_STALE ), Flags.STATE_STALE );
  }

  @Test
  public void setState()
    throws Exception
  {
    assertEquals( Flags.setState( Flags.PRIORITY_NORMAL |
                                  Flags.READ_WRITE |
                                  Flags.STATE_UP_TO_DATE,
                                  Flags.STATE_DISPOSED ),
                  Flags.PRIORITY_NORMAL | Flags.READ_WRITE | Flags.STATE_DISPOSED );
    assertEquals( Flags.setState( Flags.PRIORITY_NORMAL |
                                  Flags.READ_WRITE |
                                  Flags.STATE_UP_TO_DATE,
                                  Flags.STATE_DISPOSING ),
                  Flags.PRIORITY_NORMAL | Flags.READ_WRITE | Flags.STATE_DISPOSING );
    assertEquals( Flags.setState( Flags.PRIORITY_NORMAL |
                                  Flags.READ_WRITE |
                                  Flags.STATE_UP_TO_DATE,
                                  Flags.STATE_DISPOSING ),
                  Flags.PRIORITY_NORMAL | Flags.READ_WRITE | Flags.STATE_DISPOSING );
    assertEquals( Flags.setState( Flags.PRIORITY_NORMAL |
                                  Flags.READ_WRITE |
                                  Flags.STATE_UP_TO_DATE,
                                  Flags.STATE_INACTIVE ),
                  Flags.PRIORITY_NORMAL | Flags.READ_WRITE | Flags.STATE_INACTIVE );
    assertEquals( Flags.setState( Flags.PRIORITY_NORMAL |
                                  Flags.READ_WRITE |
                                  Flags.STATE_UP_TO_DATE,
                                  Flags.STATE_UP_TO_DATE ),
                  Flags.PRIORITY_NORMAL | Flags.READ_WRITE | Flags.STATE_UP_TO_DATE );
    assertEquals( Flags.setState( Flags.PRIORITY_NORMAL |
                                  Flags.READ_WRITE |
                                  Flags.STATE_UP_TO_DATE,
                                  Flags.STATE_POSSIBLY_STALE ),
                  Flags.PRIORITY_NORMAL | Flags.READ_WRITE | Flags.STATE_POSSIBLY_STALE );
    assertEquals( Flags.setState( Flags.PRIORITY_NORMAL |
                                  Flags.READ_WRITE |
                                  Flags.STATE_UP_TO_DATE,
                                  Flags.STATE_STALE ),
                  Flags.PRIORITY_NORMAL | Flags.READ_WRITE | Flags.STATE_STALE );
  }

  @Test
  public void getLeastStaleObserverState()
    throws Exception
  {
    assertEquals( Flags.getLeastStaleObserverState( Flags.PRIORITY_NORMAL | Flags.STATE_DISPOSED ),
                  Flags.STATE_UP_TO_DATE );
    assertEquals( Flags.getLeastStaleObserverState( Flags.PRIORITY_NORMAL | Flags.STATE_DISPOSING ),
                  Flags.STATE_UP_TO_DATE );
    assertEquals( Flags.getLeastStaleObserverState( Flags.PRIORITY_NORMAL | Flags.STATE_INACTIVE ),
                  Flags.STATE_UP_TO_DATE );
    assertEquals( Flags.getLeastStaleObserverState( Flags.PRIORITY_NORMAL | Flags.STATE_UP_TO_DATE ),
                  Flags.STATE_UP_TO_DATE );
    assertEquals( Flags.getLeastStaleObserverState( Flags.PRIORITY_NORMAL | Flags.STATE_POSSIBLY_STALE ),
                  Flags.STATE_POSSIBLY_STALE );
    assertEquals( Flags.getLeastStaleObserverState( Flags.PRIORITY_NORMAL | Flags.STATE_STALE ), Flags.STATE_STALE );
  }

  @Test
  public void getStateName()
    throws Exception
  {
    assertEquals( Flags.getStateName( Flags.STATE_DISPOSED ), "DISPOSED" );
    assertEquals( Flags.getStateName( Flags.STATE_DISPOSING ), "DISPOSING" );
    assertEquals( Flags.getStateName( Flags.STATE_INACTIVE ), "INACTIVE" );
    assertEquals( Flags.getStateName( Flags.STATE_UP_TO_DATE ), "UP_TO_DATE" );
    assertEquals( Flags.getStateName( Flags.STATE_POSSIBLY_STALE ), "POSSIBLY_STALE" );
    assertEquals( Flags.getStateName( Flags.STATE_STALE ), "STALE" );
    // State value should have been passed in
    assertEquals( Flags.getStateName( Flags.PRIORITY_NORMAL | Flags.STATE_STALE ),
                  "UNKNOWN(" + ( Flags.PRIORITY_NORMAL | Flags.STATE_STALE ) + ")" );
  }

  @Test
  public void isPrioritySpecified()
    throws Exception
  {
    assertTrue( Flags.isPrioritySpecified( Flags.PRIORITY_HIGHEST ) );
    assertTrue( Flags.isPrioritySpecified( Flags.PRIORITY_HIGH ) );
    assertTrue( Flags.isPrioritySpecified( Flags.PRIORITY_NORMAL ) );
    assertTrue( Flags.isPrioritySpecified( Flags.PRIORITY_LOW ) );
    assertTrue( Flags.isPrioritySpecified( Flags.PRIORITY_LOWEST ) );
    assertFalse( Flags.isPrioritySpecified( 0 ) );
    assertFalse( Flags.isPrioritySpecified( Flags.READ_WRITE ) );
  }

  @Test
  public void getPriority()
    throws Exception
  {
    assertEquals( Flags.getPriorityIndex( Flags.PRIORITY_HIGHEST | Flags.STATE_INACTIVE ), 0 );
    assertEquals( Flags.getPriorityIndex( Flags.PRIORITY_HIGH | Flags.STATE_INACTIVE ), 1 );
    assertEquals( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL | Flags.STATE_INACTIVE ), 2 );
    assertEquals( Flags.getPriorityIndex( Flags.PRIORITY_LOW | Flags.STATE_INACTIVE ), 3 );
    assertEquals( Flags.getPriorityIndex( Flags.PRIORITY_LOWEST | Flags.STATE_INACTIVE ), 4 );
  }

  @Test
  public void isTransactionModeSpecified()
    throws Exception
  {
    assertTrue( Flags.isTransactionModeSpecified( Flags.READ_ONLY ) );
    assertTrue( Flags.isTransactionModeSpecified( Flags.READ_WRITE ) );
    assertFalse( Flags.isTransactionModeSpecified( 0 ) );
    assertFalse( Flags.isTransactionModeSpecified( Flags.PRIORITY_LOWEST ) );
  }

  @Test
  public void isTransactionModeValid()
    throws Exception
  {
    assertTrue( Flags.isTransactionModeValid( Flags.READ_ONLY ) );
    assertTrue( Flags.isTransactionModeValid( Flags.READ_WRITE ) );
    assertFalse( Flags.isTransactionModeValid( 0 ) );
    assertFalse( Flags.isTransactionModeValid( Flags.PRIORITY_LOWEST ) );
    assertFalse( Flags.isTransactionModeValid( Flags.READ_ONLY | Flags.READ_WRITE ) );
  }

  @Test
  public void getTransactionModeName()
    throws Exception
  {
    assertEquals( Flags.getTransactionModeName( Flags.READ_ONLY ), "READ_ONLY" );
    assertEquals( Flags.getTransactionModeName( Flags.READ_WRITE ), "READ_WRITE" );
    assertEquals( Flags.getTransactionModeName( 0 ), "UNKNOWN(0)" );
  }

  @Test
  public void isNestedActionsModeValid()
    throws Exception
  {
    assertTrue( Flags.isNestedActionsModeValid( Flags.NESTED_ACTIONS_ALLOWED ) );
    assertTrue( Flags.isNestedActionsModeValid( Flags.NESTED_ACTIONS_DISALLOWED ) );
    assertFalse( Flags.isNestedActionsModeValid( 0 ) );
    assertFalse( Flags.isNestedActionsModeValid( Flags.PRIORITY_LOWEST ) );
    assertFalse( Flags.isNestedActionsModeValid( Flags.NESTED_ACTIONS_ALLOWED | Flags.NESTED_ACTIONS_DISALLOWED ) );
  }

  @Test
  public void isRunTypeValid()
    throws Exception
  {
    assertTrue( Flags.isRunTypeValid( Flags.RUN_NOW ) );
    assertTrue( Flags.isRunTypeValid( Flags.RUN_LATER ) );
    assertFalse( Flags.isRunTypeValid( 0 ) );
    assertFalse( Flags.isRunTypeValid( Flags.PRIORITY_LOWEST ) );
    assertFalse( Flags.isRunTypeValid( Flags.READ_ONLY | Flags.READ_WRITE ) );
  }

  @Test
  public void getScheduleType()
    throws Exception
  {
    assertEquals( Flags.getScheduleType( Flags.PRIORITY_HIGHEST | Flags.KEEPALIVE ), Flags.KEEPALIVE );
    assertEquals( Flags.getScheduleType( Flags.PRIORITY_HIGH | Flags.APPLICATION_EXECUTOR ),
                  Flags.APPLICATION_EXECUTOR );
    assertEquals( Flags.getScheduleType( Flags.PRIORITY_NORMAL | Flags.DEACTIVATE_ON_UNOBSERVE ),
                  Flags.DEACTIVATE_ON_UNOBSERVE );
    assertEquals( Flags.getScheduleType( Flags.PRIORITY_NORMAL ), 0 );
  }

  @Test
  public void isScheduleTypeValid()
    throws Exception
  {
    assertTrue( Flags.isScheduleTypeValid( Flags.KEEPALIVE ) );
    assertTrue( Flags.isScheduleTypeValid( Flags.DEACTIVATE_ON_UNOBSERVE ) );
    assertTrue( Flags.isScheduleTypeValid( Flags.APPLICATION_EXECUTOR ) );
    assertFalse( Flags.isScheduleTypeValid( 0 ) );
    assertFalse( Flags.isScheduleTypeValid( Flags.PRIORITY_LOWEST ) );
    assertFalse( Flags.isScheduleTypeValid( Flags.KEEPALIVE | Flags.DEACTIVATE_ON_UNOBSERVE ) );
    assertFalse( Flags.isScheduleTypeValid( Flags.KEEPALIVE | Flags.APPLICATION_EXECUTOR ) );
    assertFalse( Flags.isScheduleTypeValid( Flags.DEACTIVATE_ON_UNOBSERVE | Flags.APPLICATION_EXECUTOR ) );
  }

  @Test
  public void defaultPriorityUnlessSpecified()
  {
    assertEquals( Flags.priority( Flags.PRIORITY_HIGHEST ), 0 );
    assertEquals( Flags.priority( Flags.PRIORITY_HIGH ), 0 );
    assertEquals( Flags.priority( Flags.PRIORITY_NORMAL ), 0 );
    assertEquals( Flags.priority( Flags.PRIORITY_LOW ), 0 );
    assertEquals( Flags.priority( Flags.PRIORITY_LOWEST ), 0 );
    assertEquals( Flags.priority( 0 ), Flags.PRIORITY_NORMAL );
    assertEquals( Flags.priority( Flags.READ_ONLY ), Flags.PRIORITY_NORMAL );
  }

  @Test
  public void defaultNestedActionRuleUnlessSpecified()
  {
    assertEquals( Flags.nestedActionRule( Flags.NESTED_ACTIONS_ALLOWED ), 0 );
    assertEquals( Flags.nestedActionRule( Flags.NESTED_ACTIONS_DISALLOWED ), 0 );
    assertEquals( Flags.nestedActionRule( 0 ), Flags.NESTED_ACTIONS_DISALLOWED );
    assertEquals( Flags.nestedActionRule( Flags.READ_ONLY ), Flags.NESTED_ACTIONS_DISALLOWED );

    ArezTestUtil.noCheckInvariants();
    assertEquals( Flags.nestedActionRule( 0 ), 0 );
  }

  @Test
  public void defaultObserverTransactionModeUnlessSpecified()
  {
    assertEquals( Flags.transactionMode( Flags.READ_ONLY ), 0 );
    assertEquals( Flags.transactionMode( Flags.READ_WRITE ), 0 );
    assertEquals( Flags.transactionMode( 0 ), Flags.READ_ONLY );
    assertEquals( Flags.transactionMode( Flags.NESTED_ACTIONS_DISALLOWED ), Flags.READ_ONLY );

    ArezTestUtil.noEnforceTransactionType();
    assertEquals( Flags.transactionMode( 0 ), 0 );
  }

  @Test
  public void defaultReactTypeUnlessSpecified()
  {
    assertEquals( Flags.runType( Flags.RUN_NOW, Flags.RUN_NOW ), 0 );
    assertEquals( Flags.runType( Flags.RUN_LATER, Flags.RUN_NOW ), 0 );
    assertEquals( Flags.runType( 0, Flags.RUN_NOW ), Flags.RUN_NOW );
    assertEquals( Flags.runType( Flags.NESTED_ACTIONS_DISALLOWED, Flags.RUN_NOW ), Flags.RUN_NOW );
  }

  @Test
  public void verifyActionRule()
  {
    assertEquals( Flags.verifyActionRule( Flags.VERIFY_ACTION_REQUIRED ), 0 );
    assertEquals( Flags.verifyActionRule( Flags.NO_VERIFY_ACTION_REQUIRED ), 0 );
    assertEquals( Flags.verifyActionRule( 0 ), Flags.VERIFY_ACTION_REQUIRED );
    assertEquals( Flags.verifyActionRule( Flags.REQUIRE_NEW_TRANSACTION ), Flags.VERIFY_ACTION_REQUIRED );
  }

  @Test
  public void dependencyType()
  {
    assertEquals( Flags.dependencyType( Flags.AREZ_DEPENDENCIES ), 0 );
    assertEquals( Flags.dependencyType( Flags.AREZ_OR_NO_DEPENDENCIES ), 0 );
    assertEquals( Flags.dependencyType( Flags.AREZ_OR_EXTERNAL_DEPENDENCIES ), 0 );
    assertEquals( Flags.dependencyType( 0 ), Flags.AREZ_DEPENDENCIES );
    assertEquals( Flags.dependencyType( Flags.REQUIRE_NEW_TRANSACTION ), Flags.AREZ_DEPENDENCIES );
  }

  @Test
  public void flagsAreUnique()
    throws Exception
  {
    final HashMap<String, String> exceptions = new HashMap<>();
    exceptions.put( "VERIFY_ACTION_REQUIRED", "AREZ_DEPENDENCIES" );
    exceptions.put( "NO_VERIFY_ACTION_REQUIRED", "AREZ_OR_NO_DEPENDENCIES" );

    final HashMap<String, Integer> flags = extractFlags();
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
        if ( !innerName.equals( exceptions.get( name ) ) &&
             !name.equals( exceptions.get( innerName ) ) &&
             innerValue == value )
        {
          fail( "Flags in class " + Flags.class.getName() + " are not unique. Flag named " + name +
                " and flag named " + innerName + " have the same value: " + value );
        }
      }
    }
  }

  @Test
  public void flagsNoOverlap()
    throws Exception
  {
    // Configs with these prefixes may overlap
    final HashMap<String, String> exceptions = new HashMap<>();
    exceptions.put( "KEEPALIVE", "APPLICATION_EXECUTOR" );
    exceptions.put( "DEACTIVATE_ON_UNOBSERVE", "APPLICATION_EXECUTOR" );
    exceptions.put( "VERIFY_ACTION_REQUIRED", "AREZ_DEPENDENCIES" );
    exceptions.put( "NO_VERIFY_ACTION_REQUIRED", "AREZ_OR_NO_DEPENDENCIES" );

    // Configs with these prefixes may overlap
    final ArrayList<String> exceptionPrefixes = new ArrayList<>();
    exceptionPrefixes.add( "PRIORITY_" );
    exceptionPrefixes.add( "STATE_" );

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
        if ( !innerName.equals( exceptions.get( name ) ) &&
             !name.equals( exceptions.get( innerName ) ) &&
             exceptionPrefixes.stream().noneMatch( p -> name.startsWith( p ) && innerName.startsWith( p ) ) )
        {
          final int value = entry.getValue();
          final int innerValue = innerEntry.getValue();
          if ( ( innerValue & value ) != 0 )
          {
            fail( "Flags in class " + Flags.class.getName() + " overlap. Flag named " + name +
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
    for ( final Map.Entry<String, Integer> entry : new ArrayList<>( extractFlags().entrySet() ) )
    {
      final String name = entry.getKey();
      final int value = entry.getValue();
      if ( !name.startsWith( "UNUSED" ) &&
           ( ( Flags.CONFIG_FLAGS_MASK | Flags.RUNTIME_FLAGS_MASK | Flags.ACTION_FLAGS_MASK ) & value ) != value )
      {
        fail( "Flag named " + name + " in class " + Flags.class.getName() + " is not within " +
              "expected configuration mask. Update mask or configuration value." );
      }
    }
  }

  @Nonnull
  private HashMap<String, Integer> extractFlags()
    throws IllegalAccessException
  {
    final HashMap<String, Integer> flags = new HashMap<>();
    for ( final Field field : Flags.class.getDeclaredFields() )
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
