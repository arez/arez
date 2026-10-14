package com.example.package_access.other;

import arez.annotations.RequiresTransaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseRequiresTransactionModel
{
  @RequiresTransaction
  void perform()
  {
  }
}
