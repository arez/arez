package com.example.id;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryExampleRepositoryDaggerModule {
  @Provides
  @Singleton
  static RepositoryExampleRepository create() {
    return new Arez_RepositoryExampleRepository();
  }
}
