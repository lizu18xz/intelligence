package com.fayayo.inte.service;


import com.fayayo.inte.common.BusinessException;
import com.fayayo.inte.model.SellerModel;

import java.util.List;

public interface SellerService {

    SellerModel create(SellerModel sellerModel);

    SellerModel get(Integer id);

    List<SellerModel> selectAll();

    SellerModel changeStatus(Integer id, Integer disabledFlag) throws BusinessException;

}
