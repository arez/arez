package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;

@ArezComponent( allowEmpty = true )
abstract class BasicRequiresTransactionModel
{
  @RequiresTransaction
  void perform()
  {
  }
}
