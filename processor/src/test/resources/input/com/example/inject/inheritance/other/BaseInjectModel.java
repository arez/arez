package com.example.inject.inheritance.other;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

public abstract class BaseInjectModel
{
  @Inject
  Runnable _action;
}
