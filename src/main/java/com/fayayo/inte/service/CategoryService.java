package com.fayayo.inte.service;


import com.fayayo.inte.common.BusinessException;
import com.fayayo.inte.model.CategoryModel;

import java.util.List;

public interface CategoryService {

    CategoryModel create(CategoryModel categoryModel) throws BusinessException;

    CategoryModel get(Integer id);

    List<CategoryModel> selectAll();

    Integer countAllCategory();
}
