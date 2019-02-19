package com.maxwang.miaosha.service;

import com.maxwang.miaosha.dao.UserDao;
import com.maxwang.miaosha.domain.User;
import com.maxwang.miaosha.exception.GlobalException;
import com.maxwang.miaosha.redis.RedisService;
import com.maxwang.miaosha.redis.UserKey;
import com.maxwang.miaosha.util.CodeMsg;
import com.maxwang.miaosha.util.MD5Util;
import com.maxwang.miaosha.util.UUIDUtil;
import com.maxwang.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    public static final String  COOKIE_NAME_TOKEN = "token";

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    /**
     * 根据ID得到User
     * @param id
     * @return
     */
    public User getById(long id){
        return userDao.getById(id);
    }

    /**
     * 用户登录
     * @param response
     * @param loginVo
     * @return
     */
    public boolean login(HttpServletResponse response, LoginVo loginVo){
        if (loginVo ==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile=loginVo.getMobile();
        String formPass=loginVo.getPassword();

        User user=getById(Long.parseLong(mobile));

        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        /**
         * 验证密码
         */
        String dbPass =user.getPassword();
        String saltDb = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass,saltDb);

        if (!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token= UUIDUtil.uuid();
        addCookie(response,token,user);
        return true;
    }

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    public User getByToken(HttpServletResponse response,String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        User user = redisService.get(UserKey.token,token,User.class);
        if (user!=null){
            addCookie(response,token,user);
        }
        return user;
    }

    /**
     * 延长cookie过期时间
     * @param response
     * @param user
     */
    public void addCookie(HttpServletResponse response,String token,User user){

        redisService.set(UserKey.token,token,user);
        Cookie cookie=new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
