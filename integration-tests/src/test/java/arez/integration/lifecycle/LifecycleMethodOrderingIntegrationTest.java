package arez.integration.lifecycle;

import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnDepsChange;
import arez.annotations.PostConstruct;
import arez.annotations.PostDispose;
import arez.annotations.PreDispose;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class LifecycleMethodOrderingIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void duplicateCapableCallbacksAcrossInheritanceChain()
  {
    final DuplicateLifecycleModel model = DuplicateLifecycleModel.create();

    model.updateValue( 1 );
    model.updateValue( 5 );

    Disposable.dispose( model );

    assertEquals( trace( model.getSteps() ), """
      Grandparent.constructor
      Parent.constructor
      Component.constructor
      Grandparent.postConstruct1
      Grandparent.postConstruct2
      Parent.postConstruct1
      Parent.postConstruct2
      Component.postConstruct1
      Component.postConstruct2
      InheritedInterface.postConstruct1
      InheritedInterface.postConstruct2
      DirectInterface.postConstruct1
      DirectInterface.postConstruct2
      Component.updateValue:1
      Component.updateValue:5
      DirectInterface.preDispose2
      DirectInterface.preDispose1
      InheritedInterface.preDispose2
      InheritedInterface.preDispose1
      Component.preDispose2
      Component.preDispose1
      Parent.preDispose2
      Parent.preDispose1
      Grandparent.preDispose2
      Grandparent.preDispose1
      DirectInterface.postDispose2
      DirectInterface.postDispose1
      InheritedInterface.postDispose2
      InheritedInterface.postDispose1
      Component.postDispose2
      Component.postDispose1
      Parent.postDispose2
      Parent.postDispose1
      Grandparent.postDispose2
      Grandparent.postDispose1""".stripIndent() );
  }

  @Test
  public void observerLifecycleHooksSequenceAroundMutationAndDispose()
  {
    final ObserverLifecycleModel model = ObserverLifecycleModel.create();

    model.render();
    model.updateValue( 3 );
    model.render();

    Disposable.dispose( model );

    assertEquals( trace( model.getSteps() ), """
      Base.onTimeActivate
      Base.getTime
      Component.render:0
      Component.updateValue:3
      Base.getTime
      RenderHooks.onRenderDepsChange
      Component.render:6
      Base.onTimeDeactivate""".stripIndent() );
  }

  @Nonnull
  private static String trace( @Nonnull final List<String> steps )
  {
    return String.join( "\n", steps );
  }

  interface StepRecorder
  {
    @Nonnull
    List<String> getSteps();
  }

  interface InheritedInterfaceLifecycle
    extends StepRecorder
  {
    @PostConstruct
    default void inheritedInterfacePostConstruct1()
    {
      getSteps().add( "InheritedInterface.postConstruct1" );
    }

    @PostConstruct
    default void inheritedInterfacePostConstruct2()
    {
      getSteps().add( "InheritedInterface.postConstruct2" );
    }

    @PreDispose
    default void inheritedInterfacePreDispose1()
    {
      getSteps().add( "InheritedInterface.preDispose1" );
    }

    @PreDispose
    default void inheritedInterfacePreDispose2()
    {
      getSteps().add( "InheritedInterface.preDispose2" );
    }

    @PostDispose
    default void inheritedInterfacePostDispose1()
    {
      getSteps().add( "InheritedInterface.postDispose1" );
    }

    @PostDispose
    default void inheritedInterfacePostDispose2()
    {
      getSteps().add( "InheritedInterface.postDispose2" );
    }
  }

  interface DirectInterfaceLifecycle
    extends StepRecorder
  {
    @PostConstruct
    default void directInterfacePostConstruct1()
    {
      getSteps().add( "DirectInterface.postConstruct1" );
    }

    @PostConstruct
    default void directInterfacePostConstruct2()
    {
      getSteps().add( "DirectInterface.postConstruct2" );
    }

    @PreDispose
    default void directInterfacePreDispose1()
    {
      getSteps().add( "DirectInterface.preDispose1" );
    }

    @PreDispose
    default void directInterfacePreDispose2()
    {
      getSteps().add( "DirectInterface.preDispose2" );
    }

    @PostDispose
    default void directInterfacePostDispose1()
    {
      getSteps().add( "DirectInterface.postDispose1" );
    }

    @PostDispose
    default void directInterfacePostDispose2()
    {
      getSteps().add( "DirectInterface.postDispose2" );
    }
  }

  static abstract class GrandparentLifecycleModel
    implements InheritedInterfaceLifecycle
  {
    private final ArrayList<String> _steps = new ArrayList<>();

    GrandparentLifecycleModel()
    {
      step( "Grandparent.constructor" );
    }

    @Override
    @Nonnull
    public final List<String> getSteps()
    {
      return _steps;
    }

    protected final void step( @Nonnull final String step )
    {
      _steps.add( step );
    }

    @PostConstruct
    void grandparentPostConstruct1()
    {
      step( "Grandparent.postConstruct1" );
    }

    @PostConstruct
    void grandparentPostConstruct2()
    {
      step( "Grandparent.postConstruct2" );
    }

    @PreDispose
    void grandparentPreDispose1()
    {
      step( "Grandparent.preDispose1" );
    }

    @PreDispose
    void grandparentPreDispose2()
    {
      step( "Grandparent.preDispose2" );
    }

    @PostDispose
    void grandparentPostDispose1()
    {
      step( "Grandparent.postDispose1" );
    }

    @PostDispose
    void grandparentPostDispose2()
    {
      step( "Grandparent.postDispose2" );
    }
  }

  static abstract class ParentLifecycleModel
    extends GrandparentLifecycleModel
  {
    ParentLifecycleModel()
    {
      step( "Parent.constructor" );
    }

    @PostConstruct
    void parentPostConstruct1()
    {
      step( "Parent.postConstruct1" );
    }

    @PostConstruct
    void parentPostConstruct2()
    {
      step( "Parent.postConstruct2" );
    }

    @PreDispose
    void parentPreDispose1()
    {
      step( "Parent.preDispose1" );
    }

    @PreDispose
    void parentPreDispose2()
    {
      step( "Parent.preDispose2" );
    }

    @PostDispose
    void parentPostDispose1()
    {
      step( "Parent.postDispose1" );
    }

    @PostDispose
    void parentPostDispose2()
    {
      step( "Parent.postDispose2" );
    }
  }

  @ArezComponent
  static abstract class DuplicateLifecycleModel
    extends ParentLifecycleModel
    implements DirectInterfaceLifecycle
  {
    @Nonnull
    static DuplicateLifecycleModel create()
    {
      return new LifecycleMethodOrderingIntegrationTest_Arez_DuplicateLifecycleModel();
    }

    DuplicateLifecycleModel()
    {
      step( "Component.constructor" );
    }

    @Observable
    abstract int getValue();

    abstract void setValue( int value );

    @Action
    void updateValue( final int value )
    {
      step( "Component.updateValue:" + value );
      setValue( value );
    }

    @PostConstruct
    void componentPostConstruct1()
    {
      step( "Component.postConstruct1" );
    }

    @PostConstruct
    void componentPostConstruct2()
    {
      step( "Component.postConstruct2" );
    }

    @PreDispose
    void componentPreDispose1()
    {
      step( "Component.preDispose1" );
    }

    @PreDispose
    void componentPreDispose2()
    {
      step( "Component.preDispose2" );
    }

    @PostDispose
    void componentPostDispose1()
    {
      step( "Component.postDispose1" );
    }

    @PostDispose
    void componentPostDispose2()
    {
      step( "Component.postDispose2" );
    }
  }

  interface RenderHooks
    extends StepRecorder
  {
    @OnDepsChange
    default void onRenderDepsChange()
    {
      getSteps().add( "RenderHooks.onRenderDepsChange" );
    }
  }

  static abstract class ObserverLifecycleBase
    implements StepRecorder
  {
    private final ArrayList<String> _steps = new ArrayList<>();

    @Override
    @Nonnull
    public final List<String> getSteps()
    {
      return _steps;
    }

    protected final void step( @Nonnull final String step )
    {
      _steps.add( step );
    }

    @Observable
    abstract int getValue();

    abstract void setValue( int value );

    @Memoize
    int getTime()
    {
      step( "Base.getTime" );
      return getValue() * 2;
    }

    @OnActivate
    void onTimeActivate()
    {
      step( "Base.onTimeActivate" );
    }

    @OnDeactivate
    void onTimeDeactivate()
    {
      step( "Base.onTimeDeactivate" );
    }
  }

  @ArezComponent
  static abstract class ObserverLifecycleModel
    extends ObserverLifecycleBase
    implements RenderHooks
  {
    @Nonnull
    static ObserverLifecycleModel create()
    {
      return new LifecycleMethodOrderingIntegrationTest_Arez_ObserverLifecycleModel();
    }

    @Action
    void updateValue( final int value )
    {
      step( "Component.updateValue:" + value );
      setValue( value );
    }

    @Observe( executor = Executor.EXTERNAL )
    void render()
    {
      step( "Component.render:" + getTime() );
    }
  }
}
