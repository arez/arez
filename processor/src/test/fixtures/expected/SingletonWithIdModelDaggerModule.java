import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@SuppressWarnings("DefaultAnnotationParam")
@Generated("arez.processor.ArezProcessor")
@Module
public interface SingletonWithIdModelDaggerModule {
  @Binds
  @Singleton
  SingletonWithIdModel bindComponent(Arez_SingletonWithIdModel component);
}
