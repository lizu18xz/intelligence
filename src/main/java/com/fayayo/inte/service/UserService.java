package com.fayayo.inte.service;

import com.fayayo.inte.common.BusinessException;
import com.fayayo.inte.model.UserModel;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author dalizu on 2020/1/1.
 * @version v1.0
 * @desc
 */
public interface UserService {

    UserModel getUser(Integer id);


    UserModel register(UserModel userModel) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;


    UserModel login(String telphone, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException;


}
