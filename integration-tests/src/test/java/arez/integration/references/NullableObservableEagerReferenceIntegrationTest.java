package arez.integration.references;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class NullableObservableEagerReferenceIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final NullableObservableEagerReferenceIntegrationTest_Model2Repository repository =
      NullableObservableEagerReferenceIntegrationTest_Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final AtomicInteger findCallCount = new AtomicInteger();
    final HashMap<Object, Model2> entities = new HashMap<>();
    locator.registerLookup( Model2.class, id -> {
      findCallCount.incrementAndGet();
      return entities.get( id );
    } );

    final Model2 model2b = repository.create();
    final Object model2bId = Objects.requireNonNull( Identifiable.getArezId( model2b ) );
    entities.put( model2bId, model2b );

    assertEquals( findCallCount.get(), 0 );
    final Model1 model1 = safeAction( () -> Model1.create( null ) );
    assertEquals( findCallCount.get(), 0 );

    safeAction( () -> assertNull( model1.getModel2() ) );

    assertEquals( findCallCount.get(), 0 );

    final AtomicInteger observerCallCount = new AtomicInteger();
    final Observer observer = observer( () -> {
      model1.getModel2();
      observerCallCount.incrementAndGet();
    } );

    assertEquals( observerCallCount.get(), 1 );

    safeAction( () -> model1.setModel2Id( model2bId ) );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( findCallCount.get(), 1 );

    safeAction( () -> assertEquals( model1.getModel2(), model2b ) );

    safeAction( () -> model1.setModel2Id( null ) );

    assertEquals( observerCallCount.get(), 3 );
    assertEquals( findCallCount.get(), 1 );

    safeAction( () -> assertNull( model1.getModel2() ) );

    // Dispose to avoid error
    observer.dispose();

    Disposable.dispose( model1 );

    assertInvariant( model1::getModel2,
                     "Method named 'getModel2' invoked on disposed component named 'Model1.1'" );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nullable
    private Object _model2Id;

    static Model1 create( @Nullable final Object model2Id )
    {
      return new NullableObservableEagerReferenceIntegrationTest_Arez_Model1( model2Id );
    }

    Model1( @Nullable final Object model2Id )
    {
      _model2Id = model2Id;
    }

    @Reference
    abstract Model2 getModel2();

    @ReferenceId
    @Observable
    @Nullable
    Object getModel2Id()
    {
      return _model2Id;
    }

    void setModel2Id( @Nullable Object model2Id )
    {
      _model2Id = model2Id;
    }
  }

  @Repository( sting = Feature.DISABLE, dagger = Feature.DISABLE )
  @ArezComponent
  static abstract class Model2
  {
    @Action
    void doStuff()
    {
    }
  }
}
