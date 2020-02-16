package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import javax.inject.Singleton;
import static java.lang.annotation.RetentionPolicy.*;

@Singleton
@ArezComponent
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
