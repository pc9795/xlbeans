package com.prashantchaubey.xlbeans.beans;

import com.prashantchaubey.xlbeans.annotations.XlCell;

public class TestBeanUnsupported {

  @XlCell(position = 0)
  public Integer i;

  @XlCell(position = 1)
  public Object o;
}
