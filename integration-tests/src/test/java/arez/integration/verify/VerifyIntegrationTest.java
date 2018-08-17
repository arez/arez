package arez.integration.verify;

import arez.Arez;
import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.component.Verifiable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;

public class VerifyIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final VerifyIntegrationTest_Model2Repository repository =
      VerifyIntegrationTest_Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final HashMap<Object, Model1> entities1 = new HashMap<>();
    final HashMap<Object, Model2> entities2 = new HashMap<>();
    locator.registerLookup( Model1.class, entities1::get );
    locator.registerLookup( Model2.class, entities2::get );

    final Model2 model2a = repository.create();
    final Object model2aId = Objects.requireNonNull( Identifiable.getArezId( model2a ) );
    entities2.put( model2aId, model2a );

    final Model1 model1 = Model1.create( model2aId );
    final Object model1Id = Objects.requireNonNull( Identifiable.getArezId( model1 ) );
    entities1.put( model1Id, model1 );

    // Should verify as will link correctly
    Verifiable.verify( model1 );

    {
      entities2.clear();

      // Fail to verify as related is missing
      assertInvariant( () -> Verifiable.verify( model1 ),
                       "Reference method named 'getModel2' invoked on component named 'Model1.0' is unable to resolve entity of type arez.integration.verify.VerifyIntegrationTest.Model2 and id = 0" );
      entities2.put( model2aId, model2a );
    }

    {
      entities1.clear();

      // Fail to verify as missing self
      assertInvariant( () -> Verifiable.verify( model1 ),
                       "Attempted to lookup self in Locator with type VerifyIntegrationTest.Model1 and id '0' but unable to locate self. Actual value: null" );

      entities1.put( model1Id, model1 );
    }

    Disposable.dispose( model1 );

    // Fail to verify as disposed
    assertInvariant( () -> Verifiable.verify( model1 ),
                     "Method named 'verify' invoked on disposed component named 'Model1.0'" );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    private final Object _model2Id;

    static Model1 create( @Nonnull final Object model2Id )
    {
      return new VerifyIntegrationTest_Arez_Model1( model2Id );
    }

    Model1( @Nonnull final Object model2Id )
    {
      _model2Id = model2Id;
    }

    @Reference
    abstract Model2 getModel2();

    @ReferenceId
    @Nonnull
    final Object getModel2Id()
    {
      return _model2Id;
    }
  }

  @Repository
  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
  }
}
