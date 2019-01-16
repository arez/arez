package com.example.inject.inheritance;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import com.example.inject.inheritance.other.BaseInjectModel;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class EnhancerNeededForConsumerModel
  extends BaseInjectModel
{
  EnhancerNeededForConsumerModel()
  {
  }

  @PostConstruct
  final void postConstruct()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
