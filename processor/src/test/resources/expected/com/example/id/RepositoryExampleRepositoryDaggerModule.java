package com.example.id;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryExampleRepositoryDaggerModule {
  @Binds
  @Singleton
  RepositoryExampleRepository bindComponent(Arez_RepositoryExampleRepository component);
}
