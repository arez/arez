package com.example.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Scope;
import javax.inject.Singleton;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import static java.lang.annotation.RetentionPolicy.*;

@Singleton
@ArezComponent
@MultipleScopesModel.MyScope
public class MultipleScopesModel
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
