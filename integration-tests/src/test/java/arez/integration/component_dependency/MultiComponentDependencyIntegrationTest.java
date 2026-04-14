package arez.integration.component_dependency;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class MultiComponentDependencyIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final SharedModel sharedModel = SharedModel.create();
    final ModelUnderTest model = ModelUnderTest.create( sharedModel, sharedModel, sharedModel );
    safeAction( () -> model.setRef4( sharedModel ) );

    assertFalse( Disposable.isDisposed( sharedModel ) );
    assertFalse( Disposable.isDisposed( model ) );

    Disposable.dispose( sharedModel );

    assertTrue( Disposable.isDisposed( sharedModel ) );
    assertTrue( Disposable.isDisposed( model ) );

    assertMatchesFixture( recorder );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class SharedModel
  {
    @Nonnull
    static SharedModel create()
    {
      return new MultiComponentDependencyIntegrationTest_Arez_SharedModel();
    }
  }

  @ArezComponent
  static abstract class ModelUnderTest
  {
    @Nonnull
    @ComponentDependency
    final SharedModel _ref1;
    @Nullable
    @ComponentDependency
    final SharedModel _ref2;

    @Nonnull
    static ModelUnderTest create( @Nonnull final SharedModel ref1,
                                  @Nullable final SharedModel ref2,
                                  @Nonnull final SharedModel ref3 )
    {
      return new MultiComponentDependencyIntegrationTest_Arez_ModelUnderTest( ref1, ref2, ref3 );
    }

    ModelUnderTest( @Nonnull final SharedModel ref1, @Nullable final SharedModel ref2 )
    {
      _ref1 = Objects.requireNonNull( ref1 );
      _ref2 = ref2;
    }

    @Nonnull
    @Observable
    @ComponentDependency
    abstract SharedModel getRef3();

    abstract void setRef3( @Nonnull SharedModel model );

    @Nullable
    @Observable
    @ComponentDependency
    abstract SharedModel getRef4();

    abstract void setRef4( @Nullable SharedModel model );

  }
}
