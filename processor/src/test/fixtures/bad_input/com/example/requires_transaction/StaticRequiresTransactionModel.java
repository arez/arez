package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;

@ArezComponent
public abstract class StaticRequiresTransactionModel
{
  @RequiresTransaction
  static void perform()
  {
  }
}
