package com.fayayo.inte.dao;

import com.fayayo.inte.model.CategoryModel;

import java.util.List;

public interface CategoryModelMapper {

    int deleteByPrimaryKey(Integer id);


    int insert(CategoryModel record);

    int insertSelective(CategoryModel record);


    CategoryModel selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(CategoryModel record);


    int updateByPrimaryKey(CategoryModel record);

    List<CategoryModel> selectAll();

    int countAllCategory();


}