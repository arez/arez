package arez.integration.verify;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.TypeBasedLocator;
import arez.component.Verifiable;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class VerifyIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void verifyOnDisposeCausesException()
  {
    final Model2Repository repository = Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final HashMap<Object, Model2> entities2 = new HashMap<>();
    locator.registerLookup( Model2.class, entities2::get );

    final Model2 model2a = repository.create( 1 );
    entities2.put( model2a.getId(), model2a );

    final Model1 model1 = Model1.create( 0, model2a.getId() );

    Disposable.dispose( model1 );
    entities2.clear();

    assertInvariant( () -> Verifiable.verify( model1 ),
                     "Method named 'verify' invoked on disposed component named 'arez_integration_verify_VerifyIntegrationTest_Model1.0'" );
  }

  @Test
  public void verifySuccess()
    throws Throwable
  {
    final Model2Repository repository = Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final HashMap<Object, Model1> entities1 = new HashMap<>();
    final HashMap<Object, Model2> entities2 = new HashMap<>();
    locator.registerLookup( Model1.class, entities1::get );
    locator.registerLookup( Model2.class, entities2::get );

    final Model2 model2a = repository.create( 1 );
    entities2.put( model2a.getId(), model2a );

    final Model1 model1 = Model1.create( 0, model2a.getId() );
    entities1.put( model1.getId(), model1 );

    assertTrue( model1 instanceof Verifiable );
    assertFalse( model2a instanceof Verifiable );

    Verifiable.verify( model1 );
  }

  @Test
  public void unableToLocateOther()
  {
    final Model2Repository repository = Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final HashMap<Object, Model1> entities1 = new HashMap<>();
    final HashMap<Object, Model2> entities2 = new HashMap<>();
    locator.registerLookup( Model1.class, entities1::get );
    locator.registerLookup( Model2.class, entities2::get );

    final Model2 model2a = repository.create( 1 );
    entities2.put( model2a.getId(), model2a );

    final Model1 model1 = Model1.create( 0, model2a.getId() );
    entities1.put( model1.getId(), model1 );

    entities2.clear();

    assertInvariant( () -> Verifiable.verify( model1 ),
                     "Reference named 'model2' on component named 'arez_integration_verify_VerifyIntegrationTest_Model1.0' is unable to resolve entity of type arez.integration.verify.VerifyIntegrationTest.Model2 and id = 1" );
  }

  @Test
  public void unableToLocateSelf()
  {
    final Model2Repository repository = Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final HashMap<Object, Model2> entities2 = new HashMap<>();
    locator.registerLookup( Model2.class, entities2::get );

    final Model2 model2a = repository.create( 1 );
    entities2.put( model2a.getId(), model2a );

    assertInvariant( () -> Verifiable.verify( Model1.create( 0, model2a.getId() ) ),
                     "Attempted to lookup self in Locator with type VerifyIntegrationTest.Model1 and id '0' but unable to locate self. Actual value: null" );
  }

  @Test
  public void modelWithoutVerify()
    throws Throwable
  {
    final Model2Repository repository = Model2Repository.newRepository();
    Verifiable.verify( repository.create( 0 ) );
  }

  @ArezComponent
  static abstract class Model1
  {
    private final int _id;
    private final int _model2Id;

    static Model1 create( final int id, final int model2Id )
    {
      return new VerifyIntegrationTest_Arez_Model1( id, model2Id );
    }

    Model1( final int id, final int model2Id )
    {
      _id = id;
      _model2Id = model2Id;
    }

    @ComponentId
    int getId()
    {
      return _id;
    }

    @Reference
    abstract Model2 getModel2();

    @ReferenceId
    int getModel2Id()
    {
      return _model2Id;
    }
  }

  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
    private final int _id;

    Model2( final int id )
    {
      _id = id;
    }

    @ComponentId
    int getId()
    {
      return _id;
    }
  }

  @ArezComponent( service = Feature.ENABLE, sting = Feature.DISABLE )
  static abstract class Model2Repository
    extends AbstractRepository<Integer, Model2, Model2Repository>
  {
    @Nonnull
    static Model2Repository newRepository()
    {
      return new VerifyIntegrationTest_Arez_Model2Repository();
    }

    @Action
    Model2 create( final int id )
    {
      final VerifyIntegrationTest_Arez_Model2 entity = new VerifyIntegrationTest_Arez_Model2( id );
      attach( entity );
      return entity;
    }

    @Action
    protected void destroy( @Nonnull final Model2 entity )
    {
      super.destroy( entity );
    }
  }
}
