package arez.integration.inverse;

import arez.Arez;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class OneMultiplicityLazyLoadInverseIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final AtomicInteger locatorLookupCallCount = new AtomicInteger();

    final TypeBasedLocator locator = new TypeBasedLocator();
    final PersonRepository repository = PersonRepository.newRepository();

    Arez.context().registerLocator( locator );

    locator.registerLookup( Person.class, id -> {
      locatorLookupCallCount.incrementAndGet();
      return repository.findByArezId( (Integer) id );
    } );

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

    // Lazy forced to resolve here
    safeAction( () -> assertEquals( certificate1.getPerson(), person ) );
    safeAction( () -> assertEquals( certificate2.getPerson(), person2 ) );
    safeAction( () -> assertEquals( person.getCertificate(), certificate1 ) );
    safeAction( () -> assertEquals( person2.getCertificate(), certificate2 ) );

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

      certificate1.getPerson();
      assertEquals( locatorLookupCallCount.get(), 3 );

      certificate2.getPerson();
      assertEquals( locatorLookupCallCount.get(), 4 );
    } );

    safeAction( () -> assertEquals( certificate1.getPerson(), person2 ) );
    safeAction( () -> assertEquals( person.getCertificate(), certificate2 ) );
    safeAction( () -> assertEquals( person2.getCertificate(), certificate1 ) );

    assertEquals( inverseCallCount.get(), 3 );
    assertEquals( certificate1GetPersonCallCount.get(), 2 );
  }

  @ArezComponent
  static abstract class Certificate
  {
    @Nonnull
    static Certificate create( final int personId )
    {
      return new OneMultiplicityLazyLoadInverseIntegrationTest_Arez_Certificate( personId );
    }

    @Reference( inverseMultiplicity = Multiplicity.ONE, load = LinkType.LAZY )
    abstract Person getPerson();

    @ReferenceId
    @Observable( initializer = Feature.ENABLE )
    abstract int getPersonId();

    abstract void setPersonId( int personId );
  }

  @ArezComponent( observable = Feature.ENABLE )
  static abstract class Person
  {
    @Inverse
    @Nonnull
    abstract Certificate getCertificate();
  }

  @ArezComponent( service = Feature.ENABLE, sting = Feature.DISABLE )
  static abstract class PersonRepository
    extends AbstractRepository<Integer, Person, PersonRepository>
  {
    static PersonRepository newRepository()
    {
      return new OneMultiplicityLazyLoadInverseIntegrationTest_Arez_PersonRepository();
    }

    @Action
    Person create()
    {
      final OneMultiplicityLazyLoadInverseIntegrationTest_Arez_Person entity =
        new OneMultiplicityLazyLoadInverseIntegrationTest_Arez_Person();
      attach( entity );
      return entity;
    }

    @Action
    protected void destroy( @Nonnull final Person entity )
    {
      super.destroy( entity );
    }
  }
}
