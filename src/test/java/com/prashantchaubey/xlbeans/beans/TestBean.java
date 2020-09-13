package com.prashantchaubey.xlbeans.beans;

import com.prashantchaubey.xlbeans.annotations.XlCell;

public class TestBean {

  @XlCell(position = 0, title = "sno")
  public int sno;

  @XlCell(position = 1, title = "name")
  public String name;

  @XlCell(position = 2, title = "age")
  public int age;

  @XlCell(position = 3, title = "letter")
  public char letter;

  @Override
  public String toString() {
    return "TestBean [sno=" + sno + ", name=" + name + ", age=" + age + ", letter=" + letter + "]";
  }
}
