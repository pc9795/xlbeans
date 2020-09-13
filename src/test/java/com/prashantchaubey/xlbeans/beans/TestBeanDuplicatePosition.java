package com.prashantchaubey.xlbeans.beans;

import com.prashantchaubey.xlbeans.annotations.XlCell;

public class TestBeanDuplicatePosition {

  @XlCell(position = 0)
  public int i;

  @XlCell(position = 0)
  public byte b;
}
