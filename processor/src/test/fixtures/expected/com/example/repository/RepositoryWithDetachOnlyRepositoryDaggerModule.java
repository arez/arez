package com.example.repository;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface RepositoryWithDetachOnlyRepositoryDaggerModule {
  @Provides
  @Singleton
  static RepositoryWithDetachOnlyRepository create() {
    return new Arez_RepositoryWithDetachOnlyRepository();
  }
}
