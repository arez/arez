package com.example.requires_transaction;

import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
abstract class DefaultMethodRequiresTransactionModel
  implements DefaultRequiresTransactionMethods
{
}
