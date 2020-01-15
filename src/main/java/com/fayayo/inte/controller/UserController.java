package com.fayayo.inte.controller;

import com.fayayo.inte.common.BusinessException;
import com.fayayo.inte.common.CommonRes;
import com.fayayo.inte.common.CommonUtil;
import com.fayayo.inte.common.EmBusinessError;
import com.fayayo.inte.model.UserModel;
import com.fayayo.inte.requestReq.LoginReq;
import com.fayayo.inte.requestReq.RegisterReq;
import com.fayayo.inte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author dalizu on 2020/1/1.
 * @version v1.0
 * @desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    public static final String CURRENT_USER_SESSION = "currentUserSession";

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/get")
    public CommonRes getUser(@RequestParam(name="id")Integer id) throws BusinessException {
        UserModel userModel = userService.getUser(id);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.NO_OBJECT_FOUND);
        }else{
            return CommonRes.create(userModel);
        }

    }


    @RequestMapping("/register")
    public CommonRes register(@RequestBody RegisterReq registerReq,BindingResult bindingResult) throws BusinessException,
            UnsupportedEncodingException, NoSuchAlgorithmException {

        if(bindingResult.hasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        UserModel userModel=new UserModel();

        userModel.setTelphone(registerReq.getTelphone());
        userModel.setPassword(registerReq.getPassword());
        userModel.setNickName(registerReq.getNickName());
        userModel.setGender(registerReq.getGender());

        UserModel resUserModel = userService.register(userModel);

        userService.register(resUserModel);

        return CommonRes.create(registerReq);
    }




    @RequestMapping("/login")
    public CommonRes login(@RequestBody @Valid LoginReq loginReq, BindingResult bindingResult) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,CommonUtil.processErrorString(bindingResult));
        }
        UserModel userModel = userService.login(loginReq.getTelphone(),loginReq.getPassword());
        httpServletRequest.getSession().setAttribute(CURRENT_USER_SESSION,userModel);

        return CommonRes.create(userModel);
    }

    @RequestMapping("/logout")
    public CommonRes logout() throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        httpServletRequest.getSession().invalidate();
        return CommonRes.create(null);
    }

    //获取当前用户信息
    @RequestMapping("/getcurrentuser")
    public CommonRes getCurrentUser(){
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION);
        return CommonRes.create(userModel);
    }




}
