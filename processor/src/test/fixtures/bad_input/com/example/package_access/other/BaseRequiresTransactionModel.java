package com.example.package_access.other;

import arez.annotations.RequiresTransaction;

public abstract class BaseRequiresTransactionModel
{
  @RequiresTransaction
  void perform()
  {
  }
}
