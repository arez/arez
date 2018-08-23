package arez.integration.verify;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.TypeBasedLocator;
import arez.component.Verifiable;
import arez.integration.AbstractArezIntegrationTest;
import java.lang.reflect.Field;
import java.util.HashMap;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class VerifyOneInverseIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void verifySuccess()
    throws Throwable
  {
    final VerifyOneInverseIntegrationTest_Model2Repository repository =
      VerifyOneInverseIntegrationTest_Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final HashMap<Object, Model1> entities1 = new HashMap<>();
    final HashMap<Object, Model2> entities2 = new HashMap<>();
    locator.registerLookup( Model1.class, entities1::get );
    locator.registerLookup( Model2.class, entities2::get );

    final Model2 model2 = repository.create( 1 );
    entities2.put( model2.getId(), model2 );

    final Model1 model1 =
      Model1.create( 0, model2.getId() );
    entities1.put( model1.getId(), model1 );

    assertTrue( model1 instanceof Verifiable );
    assertTrue( model2 instanceof Verifiable );

    Verifiable.verify( model1 );
    Verifiable.verify( model2 );
  }

  @Test
  public void unableToLocateOther()
    throws Throwable
  {
    final VerifyOneInverseIntegrationTest_Model2Repository repository =
      VerifyOneInverseIntegrationTest_Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final HashMap<Object, Model1> entities1 = new HashMap<>();
    final HashMap<Object, Model2> entities2 = new HashMap<>();
    locator.registerLookup( Model1.class, entities1::get );
    locator.registerLookup( Model2.class, entities2::get );

    final Model2 model2 = repository.create( 1 );
    entities2.put( model2.getId(), model2 );

    final Model1 model1 =
      Model1.create( 0, model2.getId() );
    entities1.put( model1.getId(), model1 );

    assertTrue( model1 instanceof Verifiable );
    assertTrue( model2 instanceof Verifiable );

    Verifiable.verify( model1 );

    Verifiable.verify( model2 );

    final Field field = model1.getClass().getDeclaredField( "$$arezi$$_state" );
    field.setAccessible( true );
    field.set( model1, (byte) -1 );

    assertInvariant( () -> Verifiable.verify( model2 ),
                     "Inverse relationship named 'model1' on component named 'Model2.1' contains disposed element 'ArezComponent[Model1.0]'" );
  }

  @ArezComponent
  static abstract class Model1
  {
    private final int _id;
    private final int _model2Id;

    static Model1 create( final int id, final int model2Id )
    {
      return Arez.context().safeAction( () -> new VerifyOneInverseIntegrationTest_Arez_Model1( id, model2Id ) );
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

    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract Model2 getModel2();

    @ReferenceId
    final int getModel2Id()
    {
      return _model2Id;
    }
  }

  @Repository
  @ArezComponent
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

    @Inverse
    @Nonnull
    abstract Model1 getModel1();
  }
}
