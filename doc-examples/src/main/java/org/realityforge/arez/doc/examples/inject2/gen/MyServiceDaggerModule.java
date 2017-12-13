package org.realityforge.arez.doc.examples.inject2.gen;

import dagger.Module;
import dagger.Provides;
import javax.annotation.Nonnull;
import javax.inject.Singleton;
import org.realityforge.arez.doc.examples.inject2.Arez_MyService;
import org.realityforge.arez.doc.examples.inject2.MyService;

@Module
public interface MyServiceDaggerModule
{
  @Nonnull
  @Provides
  @Singleton
  static MyService provideComponent( final Arez_MyService component )
  {
    return component;
  }
}
