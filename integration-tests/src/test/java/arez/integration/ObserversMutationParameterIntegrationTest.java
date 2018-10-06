package arez.integration;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observe;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( { "WeakerAccess", "Duplicates" } )
public class ObserversMutationParameterIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void readOnlyAutorunAttemptsMutationScenario()
  {
    setIgnoreObserverErrors( true );

    final Model1 model = Model1.create();

    assertEquals( getObserverErrors().size(), 0 );

    Arez.context().triggerScheduler();

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( model._name, "Initial" );
    assertEquals( model._observerRunCount, 0 );
  }

  @Test
  public void readOnlyAutorunAttemptsQueryScenario()
  {
    setIgnoreObserverErrors( true );

    final Model2 model = Model2.create();

    assertEquals( getObserverErrors().size(), 0 );

    Arez.context().triggerScheduler();

    assertEquals( getObserverErrors().size(), 0 );
    assertEquals( model._name, "Initial" );
    assertEquals( model._observerRunCount, 1 );
  }

  @Test
  public void readWriteAutorunAttemptsMutationScenario()
  {
    setIgnoreObserverErrors( true );

    final Model3 model = Model3.create();

    assertEquals( getObserverErrors().size(), 0 );

    Arez.context().triggerScheduler();

    assertEquals( getObserverErrors().size(), 0 );
    assertEquals( model._name, "Changed" );
    assertEquals( model._observerRunCount, 1 );
  }

  @Test
  public void readOnlyTrackAttemptsMutationScenario()
  {
    setIgnoreObserverErrors( true );

    final Model4 model = Model4.create();

    assertEquals( getObserverErrors().size(), 0 );

    assertThrows( model::myObserveReaction );

    assertEquals( getObserverErrors().size(), 0 );
    assertEquals( model._name, "Initial" );
    assertEquals( model._observerRunCount, 0 );
  }

  @Test
  public void readOnlyTrackAttemptsQueryScenario()
  {
    setIgnoreObserverErrors( true );

    final Model5 model = Model5.create();

    assertEquals( getObserverErrors().size(), 0 );

    model.myObserveReaction();

    assertEquals( getObserverErrors().size(), 0 );
    assertEquals( model._name, "Initial" );
    assertEquals( model._observerRunCount, 1 );
  }

  @Test
  public void readWriteTrackAttemptsMutationScenario()
  {
    setIgnoreObserverErrors( true );

    final Model6 model = Model6.create();

    assertEquals( getObserverErrors().size(), 0 );

    model.myObserveReaction();

    assertEquals( getObserverErrors().size(), 0 );
    assertEquals( model._name, "Changed" );
    assertEquals( model._observerRunCount, 1 );
  }

  @ArezComponent( deferSchedule = true )
  public static abstract class Model1
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    public static Model1 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model1();
    }

    @Observe
    void myObserveReaction()
    {
      observeADependency();
      setName( "Changed" );
      _observerRunCount += 1;
    }

    @Observable
    public String getName()
    {
      return _name;
    }

    public void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent( deferSchedule = true )
  public static abstract class Model2
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    public static Model2 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model2();
    }

    @SuppressWarnings( "ResultOfMethodCallIgnored" )
    @Observe
    void myObserveReaction()
    {
      getName();
      _observerRunCount += 1;
    }

    @Observable
    public String getName()
    {
      return _name;
    }

    public void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent( deferSchedule = true )
  public static abstract class Model3
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    public static Model3 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model3();
    }

    @Observe( mutation = true )
    void myObserveReaction()
    {
      observeADependency();
      setName( "Changed" );
      _observerRunCount += 1;
    }

    @Observable
    public String getName()
    {
      return _name;
    }

    public void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  public static abstract class Model4
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    public static Model4 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model4();
    }

    @Observe( executor = Executor.APPLICATION )
    public void myObserveReaction()
    {
      setName( "Changed" );
      _observerRunCount += 1;
    }

    void onMyObserveReactionDepsChanged()
    {
    }

    @Observable
    public String getName()
    {
      return _name;
    }

    public void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  public static abstract class Model5
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    public static Model5 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model5();
    }

    @SuppressWarnings( "ResultOfMethodCallIgnored" )
    @Observe( executor = Executor.APPLICATION )
    public void myObserveReaction()
    {
      getName();
      _observerRunCount += 1;
    }

    void onMyObserveReactionDepsChanged()
    {
    }

    @Observable
    public String getName()
    {
      return _name;
    }

    public void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  public static abstract class Model6
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    public static Model6 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model6();
    }

    @Observe( executor = Executor.APPLICATION, mutation = true )
    public void myObserveReaction()
    {
      observeADependency();
      setName( "Changed" );
      _observerRunCount += 1;
    }

    void onMyObserveReactionDepsChanged()
    {
    }

    @Observable
    public String getName()
    {
      return _name;
    }

    public void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }
}
