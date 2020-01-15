package com.fayayo.inte.service.impl;

import com.fayayo.inte.common.BusinessException;
import com.fayayo.inte.common.EmBusinessError;
import com.fayayo.inte.dao.UserModelMapper;
import com.fayayo.inte.model.UserModel;
import com.fayayo.inte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author dalizu on 2020/1/1.
 * @version v1.0
 * @desc
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserModelMapper userModelMapper;


    @Override
    public UserModel getUser(Integer id) {


        return userModelMapper.selectByPrimaryKey(id);
    }


    @Transactional
    @Override
    public UserModel register(UserModel userModel) throws BusinessException, UnsupportedEncodingException,
            NoSuchAlgorithmException {
        userModel.setPassword(encodeByMd5(userModel.getPassword()));
        userModel.setCreatedAt(new Date());
        userModel.setUpdatedAt(new Date());

        try {
            userModelMapper.insertSelective(userModel);
        }catch (DuplicateKeyException ex){

            throw new BusinessException(EmBusinessError.REGISTER_DUP_FAIL);
        }
        return getUser(userModel.getId());
    }

    @Override
    public UserModel login(String telphone, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {


        UserModel userModel=userModelMapper.selectByTelphoneAndPassword(telphone,encodeByMd5(password));

        if(userModel==null){
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }

        return userModel;
    }

    public String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest=MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder=new BASE64Encoder();
        return base64Encoder.encode(messageDigest.digest(str.getBytes("utf-8")));
    }


}
