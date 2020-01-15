package com.fayayo.inte.controller;

import com.fayayo.inte.common.BusinessException;
import com.fayayo.inte.common.CommonRes;
import com.fayayo.inte.common.EmBusinessError;
import com.fayayo.inte.model.CategoryModel;
import com.fayayo.inte.model.ShopModel;
import com.fayayo.inte.service.CategoryService;
import com.fayayo.inte.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dalizu on 2020/1/4.
 * @version v1.0
 * @desc
 */
@Controller
@RequestMapping("/shop/")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;


    //推荐服务V1.0
    @RequestMapping("recommend")
    @ResponseBody
    public CommonRes recommend(@RequestParam("longitude")BigDecimal longitude,
                               @RequestParam("latitude")BigDecimal latitude) throws BusinessException {

        if(latitude==null||latitude==null){
            throw new BusinessException(EmBusinessError.BIND_EXCEPTION_ERROR);
        }

        List<ShopModel>shopModelList=shopService.recommend(longitude,latitude);
        return CommonRes.create(shopModelList);
    }


    //搜索服务V1.0
    @RequestMapping("search")
    @ResponseBody
    public CommonRes search(@RequestParam("longitude")BigDecimal longitude,
                               @RequestParam("latitude")BigDecimal latitude,
                            @RequestParam("keyword")String keyword,
                            @RequestParam(value = "orderby",required = false)Integer orderby,
                            @RequestParam(value = "categoryId",required = false)Integer categoryId,
                            @RequestParam(value = "tags",required = false)String tags) throws BusinessException, IOException {

        if(StringUtils.isEmpty(keyword)||longitude==null||latitude==null){
            throw new BusinessException(EmBusinessError.BIND_EXCEPTION_ERROR);
        }

        //List<ShopModel>shopModelList=shopService.search(longitude,latitude,keyword,orderby,categoryId,tags);
        List<CategoryModel>categoryModels=categoryService.selectAll();
        //List<Map<String,Object>>tagsAgg=shopService.searchGroupByTags(keyword,categoryId,tags);


        Map<String,Object>result= shopService.searchEs(longitude,latitude,keyword,orderby,categoryId,tags);

        List<ShopModel>shopModelList= (List<ShopModel>) result.get("shop");
        List<Map<String,Object>>tagsAgg= (List<Map<String, Object>>) result.get("tags");

        Map<String,Object> resMap=new HashMap<>();
        resMap.put("shop",shopModelList);
        resMap.put("category",categoryModels);
        resMap.put("tags",tagsAgg);
        return CommonRes.create(resMap);

    }





}
