package com.example.repository;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithCreateOrAttachRepositoryDaggerModule {
  @Binds
  @Singleton
  RepositoryWithCreateOrAttachRepository bindComponent(
      Arez_RepositoryWithCreateOrAttachRepository component);
}
