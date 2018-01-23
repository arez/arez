import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface ImplicitSingletonModelDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static ImplicitSingletonModel provideComponent(final Arez_ImplicitSingletonModel component) {
    return component;
  }
}
