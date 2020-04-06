package arez.integration.inverse;

import arez.Arez;
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
import arez.component.Linkable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class OneMultiplicityExplicitLoadInverseIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final AtomicInteger locatorLookupCallCount = new AtomicInteger();

    final TypeBasedLocator locator = new TypeBasedLocator();
    final OneMultiplicityExplicitLoadInverseIntegrationTest_PersonRepository repository =
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
    final Certificate certificate2 = Certificate.create( person2Id );

    assertEquals( locatorLookupCallCount.get(), 0 );
    assertEquals( inverseCallCount.get(), 1 );

    safeAction( () -> Linkable.link( certificate1 ) );
    safeAction( () -> Linkable.link( certificate2 ) );

    assertEquals( locatorLookupCallCount.get(), 2 );
    assertEquals( inverseCallCount.get(), 2 );

    final AtomicInteger certificate1GetPersonCallCount = new AtomicInteger();
    observer( () -> {
      certificate1.getPerson();
      certificate1GetPersonCallCount.incrementAndGet();
    } );

    final AtomicInteger certificate2GetPersonCallCount = new AtomicInteger();
    observer( () -> {
      certificate2.getPerson();
      certificate2GetPersonCallCount.incrementAndGet();
    } );

    safeAction( () -> assertEquals( certificate1.getPerson(), person ) );
    safeAction( () -> assertEquals( certificate2.getPerson(), person2 ) );
    safeAction( () -> assertEquals( person.getCertificate(), certificate1 ) );
    safeAction( () -> assertEquals( person2.getCertificate(), certificate2 ) );

    assertEquals( certificate1GetPersonCallCount.get(), 1 );
    assertEquals( certificate2GetPersonCallCount.get(), 1 );
    assertEquals( locatorLookupCallCount.get(), 2 );
    assertEquals( inverseCallCount.get(), 2 );

    safeAction( () -> {
      certificate1.setPersonId( person2Id );
      certificate2.setPersonId( personId );
      assertEquals( certificate1GetPersonCallCount.get(), 1 );
      assertEquals( certificate2GetPersonCallCount.get(), 1 );
      assertEquals( locatorLookupCallCount.get(), 2 );
      assertEquals( inverseCallCount.get(), 2 );

      Linkable.link( certificate1 );
      assertEquals( locatorLookupCallCount.get(), 3 );

      Linkable.link( certificate2 );
      assertEquals( locatorLookupCallCount.get(), 4 );
    } );

    safeAction( () -> assertEquals( certificate1.getPerson(), person2 ) );
    safeAction( () -> assertEquals( person.getCertificate(), certificate2 ) );
    safeAction( () -> assertEquals( person2.getCertificate(), certificate1 ) );

    assertEquals( inverseCallCount.get(), 3 );
    assertEquals( certificate1GetPersonCallCount.get(), 2 );
  }

  @Nonnull
  private OneMultiplicityExplicitLoadInverseIntegrationTest_PersonRepository createPersonRepository( @Nonnull final TypeBasedLocator locator,
                                                                                                     @Nonnull final AtomicInteger lookupCallCount )
  {
    final OneMultiplicityExplicitLoadInverseIntegrationTest_PersonRepository repository =
      OneMultiplicityExplicitLoadInverseIntegrationTest_PersonRepository.newRepository();

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
      return new OneMultiplicityExplicitLoadInverseIntegrationTest_Arez_Certificate( personId );
    }

    @Reference( inverseMultiplicity = Multiplicity.ONE, load = LinkType.EXPLICIT )
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
    @Nonnull
    abstract Certificate getCertificate();
  }
}
