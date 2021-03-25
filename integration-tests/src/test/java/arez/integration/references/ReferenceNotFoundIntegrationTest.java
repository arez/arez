package arez.integration.references;

import arez.Arez;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;

public final class ReferenceNotFoundIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final Model2Repository repository = Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final Model2 model2 = repository.create();
    final Object model2Id = Objects.requireNonNull( Identifiable.getArezId( model2 ) );

    assertInvariant( () -> safeAction( () -> Model1.create( model2Id ) ),
                     "Reference named 'model2' on component named 'arez_integration_references_ReferenceNotFoundIntegrationTest_Model1.1' is unable to resolve entity of type arez.integration.references.ReferenceNotFoundIntegrationTest.Model2 and id = 1" );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    private Object _model2Id;

    static Model1 create( @Nonnull final Object model2Id )
    {
      return new ReferenceNotFoundIntegrationTest_Arez_Model1( model2Id );
    }

    Model1( @Nonnull final Object model2Id )
    {
      _model2Id = model2Id;
    }

    @Reference
    abstract Model2 getModel2();

    @ReferenceId
    @Observable
    @Nonnull
    Object getModel2Id()
    {
      return _model2Id;
    }

    void setModel2Id( @Nonnull Object model2Id )
    {
      _model2Id = model2Id;
    }
  }

  @ArezComponent( requireId = Feature.ENABLE )
  static abstract class Model2
  {
    @Action
    void doStuff()
    {
    }
  }

  @ArezComponent( service = Feature.ENABLE, dagger = Feature.DISABLE, sting = Feature.DISABLE )
  static abstract class Model2Repository
    extends AbstractRepository<Integer, Model2, Model2Repository>
  {
    @Nonnull
    static Model2Repository newRepository()
    {
      return new ReferenceNotFoundIntegrationTest_Arez_Model2Repository();
    }

    @Action
    Model2 create()
    {
      final ReferenceNotFoundIntegrationTest_Arez_Model2 entity =
        new ReferenceNotFoundIntegrationTest_Arez_Model2();
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
