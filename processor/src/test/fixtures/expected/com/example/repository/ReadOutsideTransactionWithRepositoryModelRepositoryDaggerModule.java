package com.example.repository;

import dagger.Binds;
import dagger.Module;
import javax.annotation.Generated;
import javax.inject.Singleton;

@Generated("arez.processor.ArezProcessor")
@Module
public interface ReadOutsideTransactionWithRepositoryModelRepositoryDaggerModule {
  @Binds
  @Singleton
  ReadOutsideTransactionWithRepositoryModelRepository bindComponent(
      Arez_ReadOutsideTransactionWithRepositoryModelRepository component);
}
