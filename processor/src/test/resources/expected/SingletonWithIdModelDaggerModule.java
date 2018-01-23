import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface SingletonWithIdModelDaggerModule {
  @Nonnull
  @Provides
  @Singleton
  static SingletonWithIdModel provideComponent(final Arez_SingletonWithIdModel component) {
    return component;
  }
}
