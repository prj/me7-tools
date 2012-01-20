package com.prj.tuning.xdf.olsimpl;

import com.prj.tuning.xdf.binding.XdfCategoryMem;

public class OlsCategoryMem extends XdfCategoryMem {
  private int categoryId;
  
  public OlsCategoryMem(int categoryId) {
    this.categoryId = categoryId;
  }

  @Override
  public int getIndex() {
    return 0;
  }

  @Override
  public int getCategory() {
    return categoryId;
  }

}
