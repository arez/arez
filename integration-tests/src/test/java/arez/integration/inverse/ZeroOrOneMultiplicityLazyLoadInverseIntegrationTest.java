package arez.integration.inverse;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ZeroOrOneMultiplicityLazyLoadInverseIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final AtomicInteger locatorLookupCallCount = new AtomicInteger();

    final TypeBasedLocator locator = new TypeBasedLocator();
    final ZeroOrOneMultiplicityLazyLoadInverseIntegrationTest_PersonRepository repository =
      createPersonRepository( locator, locatorLookupCallCount );

    final Person person = repository.create();
    final Person person2 = repository.create();
    final Integer personId = Objects.requireNonNull( Identifiable.getArezId( person ) );
    final Integer person2Id = Objects.requireNonNull( Identifiable.getArezId( person2 ) );

    assertEquals( locatorLookupCallCount.get(), 0 );

    final AtomicInteger inverseCallCount = new AtomicInteger();
    observer( () -> {
      person.getCertificate();
      inverseCallCount.incrementAndGet();
    } );

    assertEquals( locatorLookupCallCount.get(), 0 );
    assertEquals( inverseCallCount.get(), 1 );
    final Certificate certificate1 = Certificate.create( personId );

    assertEquals( locatorLookupCallCount.get(), 0 );
    assertEquals( inverseCallCount.get(), 1 );

    // Resolve lazy reference here
    safeAction( () -> assertEquals( certificate1.getPerson(), person ) );
    safeAction( () -> assertEquals( person.getCertificate(), certificate1 ) );
    safeAction( () -> assertNull( person2.getCertificate() ) );

    assertEquals( locatorLookupCallCount.get(), 1 );
    assertEquals( inverseCallCount.get(), 2 );

    final AtomicInteger certificate1GetPersonCallCount = new AtomicInteger();
    final Observer certificate1GetPersonObserver = observer( () -> {
      certificate1.getPerson();
      certificate1GetPersonCallCount.incrementAndGet();
    } );

    assertEquals( certificate1GetPersonCallCount.get(), 1 );
    assertEquals( locatorLookupCallCount.get(), 1 );

    safeAction( () -> {
      certificate1.setPersonId( person2Id );
      assertEquals( locatorLookupCallCount.get(), 1 );
      assertEquals( certificate1.getPerson(), person2 );
      assertNull( person.getCertificate() );
      assertEquals( person2.getCertificate(), certificate1 );
      assertEquals( locatorLookupCallCount.get(), 2 );
    } );

    assertEquals( inverseCallCount.get(), 3 );
    assertEquals( certificate1GetPersonCallCount.get(), 2 );

    // Stop observer so we don't get an exception
    Disposable.dispose( certificate1GetPersonObserver );

    Disposable.dispose( certificate1 );

    assertTrue( Disposable.isDisposed( certificate1 ) );
    safeAction( () -> assertNull( person.getCertificate() ) );
    safeAction( () -> assertNull( person2.getCertificate() ) );

    assertEquals( certificate1GetPersonCallCount.get(), 2 );
    assertEquals( locatorLookupCallCount.get(), 2 );
  }

  @Nonnull
  private ZeroOrOneMultiplicityLazyLoadInverseIntegrationTest_PersonRepository createPersonRepository( @Nonnull final TypeBasedLocator locator,
                                                                                                       @Nonnull final AtomicInteger lookupCallCount )
  {
    final ZeroOrOneMultiplicityLazyLoadInverseIntegrationTest_PersonRepository repository =
      ZeroOrOneMultiplicityLazyLoadInverseIntegrationTest_PersonRepository.newRepository();

    Arez.context().registerLocator( locator );

    locator.registerLookup( Person.class, id -> {
      lookupCallCount.incrementAndGet();
      return repository.findByArezId( (Integer) id );
    } );
    return repository;
  }

  @ArezComponent
  static abstract class Certificate
  {
    @Nonnull
    static Certificate create( final int personId )
    {
      return new ZeroOrOneMultiplicityLazyLoadInverseIntegrationTest_Arez_Certificate( personId );
    }

    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE, load = LinkType.LAZY )
    abstract Person getPerson();

    @ReferenceId
    @Observable( initializer = Feature.ENABLE )
    abstract int getPersonId();

    abstract void setPersonId( int personId );
  }

  @Repository( sting = Feature.DISABLE, dagger = Feature.DISABLE )
  @ArezComponent
  static abstract class Person
  {
    @Inverse
    @Nullable
    abstract Certificate getCertificate();
  }
}
