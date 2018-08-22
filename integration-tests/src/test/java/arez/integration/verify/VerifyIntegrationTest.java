package arez.integration.verify;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.TypeBasedLocator;
import arez.component.Verifiable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import org.testng.annotations.Test;

public class VerifyIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void verifyOnDisposeCausesException()
    throws Throwable
  {
    final VerifyIntegrationTest_Model2Repository repository = VerifyIntegrationTest_Model2Repository.newRepository();

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
                     "Method named 'verify' invoked on disposed component named 'Model1.0'" );
  }

  @Test
  public void verifySuccess()
    throws Throwable
  {
    final VerifyIntegrationTest_Model2Repository repository = VerifyIntegrationTest_Model2Repository.newRepository();

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

    Verifiable.verify( model1 );
  }

  @Test
  public void unableToLocateOther()
    throws Throwable
  {
    final VerifyIntegrationTest_Model2Repository repository = VerifyIntegrationTest_Model2Repository.newRepository();

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
                     "Reference named 'model2' on component named 'Model1.0' is unable to resolve entity of type arez.integration.verify.VerifyIntegrationTest.Model2 and id = 1" );
  }

  @Test
  public void unableToLocateSelf()
    throws Throwable
  {
    final VerifyIntegrationTest_Model2Repository repository = VerifyIntegrationTest_Model2Repository.newRepository();

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
    final VerifyIntegrationTest_Model2Repository repository = VerifyIntegrationTest_Model2Repository.newRepository();
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
    final int getId()
    {
      return _id;
    }

    @Reference
    abstract Model2 getModel2();

    @ReferenceId
    final int getModel2Id()
    {
      return _model2Id;
    }
  }

  @Repository
  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
    private final int _id;

    Model2( final int id )
    {
      _id = id;
    }

    @ComponentId
    final int getId()
    {
      return _id;
    }
  }
}
