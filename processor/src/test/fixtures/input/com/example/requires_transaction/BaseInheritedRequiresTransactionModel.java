package com.example.requires_transaction;

import arez.annotations.RequiresTransaction;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class BaseInheritedRequiresTransactionModel
{
  @RequiresTransaction
  void performBase()
  {
  }
}
