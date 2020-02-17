package com.example.dagger;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import javax.inject.Singleton;
import static java.lang.annotation.RetentionPolicy.*;

@Singleton
@ArezComponent( sting = Feature.DISABLE )
@MultipleJsr330ScopesModel.MyScope
public abstract class MultipleJsr330ScopesModel
{
  @Scope
  @Documented
  @Retention( RUNTIME )
  public @interface MyScope
  {
  }

  @Action
  void myAction()
  {
  }
}
