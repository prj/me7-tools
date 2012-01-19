package com.prj.tuning.xdf.olsimpl;

import java.util.Arrays;
import java.util.Collection;

import com.prj.tuning.mappack.map.Value;
import com.prj.tuning.xdf.binding.XdfMath;
import com.prj.tuning.xdf.binding.XdfMathVar;

public class OlsMath extends XdfMath {
  private Value value;

  public OlsMath(Value value) {
    this.value = value;
  }

  @Override
  public String getXdfEquation() {
    return value.getOffset() + "+X*" + value.getFactor();
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
