package arez.integration;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.Observable;
import arez.annotations.Track;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( { "WeakerAccess", "Duplicates" } )
public class ObserversMutationParameterIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void readOnlyAutorunAttemptsMutationScenario()
    throws Throwable
  {
    setPrintObserverErrors( false );
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
    throws Throwable
  {
    setPrintObserverErrors( false );
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
    throws Throwable
  {
    setPrintObserverErrors( false );
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
    throws Throwable
  {
    setPrintObserverErrors( false );
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
    throws Throwable
  {
    setPrintObserverErrors( false );
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
    throws Throwable
  {
    setPrintObserverErrors( false );
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

    @Autorun
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
    @Autorun
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

    @Autorun( mutation = true )
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

    @Track
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
    @Track
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

    @Track( mutation = true )
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
}
