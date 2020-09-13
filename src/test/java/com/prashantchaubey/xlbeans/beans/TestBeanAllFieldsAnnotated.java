package com.prashantchaubey.xlbeans.beans;

import com.prashantchaubey.xlbeans.annotations.XlCell;

public class TestBeanAllFieldsAnnotated extends TestBeanSuper {

  @XlCell(position = 2)
  private int i1;

  @XlCell(position = 3)
  private Integer i2;

  @XlCell(position = 4)
  private byte b1;

  @XlCell(position = 5)
  private Byte b2;

  @XlCell(position = 6)
  private short s1;

  @XlCell(position = 7)
  private Short s2;

  @XlCell(position = 8)
  private long l1;

  @XlCell(position = 9)
  private Long l2;

  @XlCell(position = 10)
  private float f1;

  @XlCell(position = 11)
  private Float f2;

  @XlCell(position = 12)
  private double d1;

  @XlCell(position = 13)
  private Double d2;

  @XlCell(position = 14)
  private char c1;

  @XlCell(position = 15)
  private Character c2;

  @XlCell(position = 16)
  private String str;

  public int getI1() {
    return i1;
  }

  public void setI1(int i1) {
    this.i1 = i1;
  }

  public Integer getI2() {
    return i2;
  }

  public void setI2(Integer i2) {
    this.i2 = i2;
  }

  public byte getB1() {
    return b1;
  }

  public void setB1(byte b1) {
    this.b1 = b1;
  }

  public Byte getB2() {
    return b2;
  }

  public void setB2(Byte b2) {
    this.b2 = b2;
  }

  public short getS1() {
    return s1;
  }

  public void setS1(short s1) {
    this.s1 = s1;
  }

  public Short getS2() {
    return s2;
  }

  public void setS2(Short s2) {
    this.s2 = s2;
  }

  public long getL1() {
    return l1;
  }

  public void setL1(long l1) {
    this.l1 = l1;
  }

  public Long getL2() {
    return l2;
  }

  public void setL2(Long l2) {
    this.l2 = l2;
  }

  public float getF1() {
    return f1;
  }

  public void setF1(float f1) {
    this.f1 = f1;
  }

  public Float getF2() {
    return f2;
  }

  public void setF2(Float f2) {
    this.f2 = f2;
  }

  public double getD1() {
    return d1;
  }

  public void setD1(double d1) {
    this.d1 = d1;
  }

  public Double getD2() {
    return d2;
  }

  public void setD2(Double d2) {
    this.d2 = d2;
  }

  public char getC1() {
    return c1;
  }

  public void setC1(char c1) {
    this.c1 = c1;
  }

  public Character getC2() {
    return c2;
  }

  public void setC2(Character c2) {
    this.c2 = c2;
  }

  public String getStr() {
    return str;
  }

  public void setStr(String str) {
    this.str = str;
  }
}
