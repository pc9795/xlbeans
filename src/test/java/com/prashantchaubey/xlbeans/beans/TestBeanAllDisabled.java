package com.prashantchaubey.xlbeans.beans;

import com.prashantchaubey.xlbeans.annotations.Disabled;
import com.prashantchaubey.xlbeans.annotations.XlCell;

public class TestBeanAllDisabled {

  @Disabled public Integer i;

  @XlCell(position = 0)
  @Disabled
  public Long l;

  @Disabled public String str;
}
