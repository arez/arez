package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ObserverRef;

interface ObserverRefInterface
{
  @ObserverRef
  Observer getDoStuffObserver();
}
