package arez.integration.collections;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import arez.integration.AbstractIntegrationTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class UnmodifiableSetterlessCollectionsIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Model1 m1 = Model1.create();

    // In the future this should actuall no throw an exception
    assertThrows( () -> context.safeAction( () -> assertUnmodifiable( m1.getValue(), ValueUtil::randomString ) ) );

    // Reference identity should be guaranteed
    context.safeAction( () -> assertTrue( m1.getValue() == m1.getValue() ) );

    final HashSet<String> value = new HashSet<>( Arrays.asList( "1", "2", "3" ) );

    context.safeAction( () -> m1.setValue( value ) );

    // In the future this should actuall no throw an exception
    assertThrows( () -> context.safeAction( () -> assertUnmodifiable( m1.getValue(), ValueUtil::randomString ) ) );

    // Assert Collections contain expected
    context.safeAction( () -> assertTrue( m1.getValue().contains( value.iterator().next() ) ) );

    // Reference identity should be guaranteed
    context.safeAction( () -> assertTrue( m1.getValue() == m1.getValue() ) );
  }

  @Test
  public void scenario_when_CollectionPropertiesModifiable()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Model1 m1 = Model1.create();

    assertThrows( () -> context.safeAction( () -> assertUnmodifiable( m1.getValue(), ValueUtil::randomString ) ) );

    // Reference identity should be guaranteed
    context.safeAction( () -> assertTrue( m1.getValue() == m1.getValue() ) );

    final HashSet<String> value = new HashSet<>( Arrays.asList( "1", "2", "3" ) );

    context.safeAction( () -> m1.setValue( value ) );

    assertThrows( () -> context.safeAction( () -> assertUnmodifiable( m1.getValue(), ValueUtil::randomString ) ) );

    // Assert Collections contain expected
    context.safeAction( () -> assertTrue( m1.getValue().contains( value.iterator().next() ) ) );

    // Reference identity should be guaranteed
    context.safeAction( () -> assertTrue( m1.getValue() == m1.getValue() ) );
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
    private HashSet<String> _value = new HashSet<>();

    @Nonnull
    static Model1 create()
    {
      return new UnmodifiableSetterlessCollectionsIntegrationTest_Arez_Model1();
    }

    @Observable( expectSetter = false )
    @Nonnull
    Collection<String> getValue()
    {
      return _value;
    }

    void setValue( @Nonnull final HashSet<String> value )
    {
      getValueObservable().preReportChanged();
      _value = value;
      getValueObservable().reportChanged();
    }

    @ObservableRef
    abstract arez.Observable getValueObservable();
  }
}
