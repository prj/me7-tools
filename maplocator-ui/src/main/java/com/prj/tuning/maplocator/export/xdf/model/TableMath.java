package com.prj.tuning.maplocator.export.xdf.model;

import java.util.Arrays;
import java.util.Collection;

import com.prj.tuning.xdf.binding.XdfMath;
import com.prj.tuning.xdf.binding.XdfMathVar;

public class TableMath extends XdfMath {
  private double factor;
  private double offset;

  public TableMath(double factor, double offset) {
    this.factor = factor;
    this.offset = offset;
  }

  @Override
  public String getXdfEquation() {
    return String.format("%f+X*%f", offset, factor);
  }

  @Override
  public Collection<XdfMathVar> getXdfMathVars() {
    return Arrays.asList(new XdfMathVar[] { new XdfMathVar() {
      @Override
      public String getId() {
        return "X";
      }
    } });
  }

}
