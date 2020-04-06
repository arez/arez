package arez.integration;

import arez.Arez;
import arez.SchedulerLock;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observe;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObserversMutationParameterIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void readOnlyAutorunAttemptsMutationScenario()
  {
    captureObserverErrors();

    final SchedulerLock lock = Arez.context().pauseScheduler();
    final Model1 model = Model1.create();

    assertEquals( getObserverErrors().size(), 0 );

    lock.dispose();

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( model._name, "Initial" );
    assertEquals( model._observerRunCount, 0 );
  }

  @Test
  public void readOnlyAutorunAttemptsQueryScenario()
  {
    captureObserverErrors();

    final SchedulerLock lock = Arez.context().pauseScheduler();
    final Model2 model = Model2.create();

    assertEquals( getObserverErrors().size(), 0 );

    lock.dispose();

    assertEquals( getObserverErrors().size(), 0 );
    assertEquals( model._name, "Initial" );
    assertEquals( model._observerRunCount, 1 );
  }

  @Test
  public void readWriteAutorunAttemptsMutationScenario()
  {
    captureObserverErrors();

    final SchedulerLock lock = Arez.context().pauseScheduler();
    final Model3 model = Model3.create();

    assertEquals( getObserverErrors().size(), 0 );

    lock.dispose();

    assertEquals( getObserverErrors().size(), 0 );
    assertEquals( model._name, "Changed" );
    assertEquals( model._observerRunCount, 1 );
  }

  @Test
  public void readOnlyTrackAttemptsMutationScenario()
  {
    captureObserverErrors();

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
    captureObserverErrors();

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
    captureObserverErrors();

    final Model6 model = Model6.create();

    assertEquals( getObserverErrors().size(), 0 );

    model.myObserveReaction();

    assertEquals( getObserverErrors().size(), 0 );
    assertEquals( model._name, "Changed" );
    assertEquals( model._observerRunCount, 1 );
  }

  @ArezComponent
  static abstract class Model1
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    static Model1 create()
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
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  static abstract class Model2
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    static Model2 create()
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
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  static abstract class Model3
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    static Model3 create()
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
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  static abstract class Model4
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    static Model4 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model4();
    }

    @Observe( executor = Executor.EXTERNAL )
    void myObserveReaction()
    {
      setName( "Changed" );
      _observerRunCount += 1;
    }

    void onMyObserveReactionDepsChange()
    {
    }

    @Observable
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  static abstract class Model5
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    static Model5 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model5();
    }

    @SuppressWarnings( "ResultOfMethodCallIgnored" )
    @Observe( executor = Executor.EXTERNAL )
    void myObserveReaction()
    {
      getName();
      _observerRunCount += 1;
    }

    void onMyObserveReactionDepsChange()
    {
    }

    @Observable
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }

  @ArezComponent
  static abstract class Model6
  {
    String _name = "Initial";
    int _observerRunCount;

    @Nonnull
    static Model6 create()
    {
      return new ObserversMutationParameterIntegrationTest_Arez_Model6();
    }

    @Observe( executor = Executor.EXTERNAL, mutation = true )
    void myObserveReaction()
    {
      observeADependency();
      setName( "Changed" );
      _observerRunCount += 1;
    }

    void onMyObserveReactionDepsChange()
    {
    }

    @Observable
    String getName()
    {
      return _name;
    }

    void setName( @Nonnull final String name )
    {
      _name = name;
    }
  }
}
