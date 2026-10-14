package com.example.requires_transaction;

import arez.annotations.ArezComponent;
import arez.annotations.RequiresTransaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class AllowEmptyRequiresTransactionModel
{
  @RequiresTransaction
  void perform()
  {
  }
}
