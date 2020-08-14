package gms.cuit.service;

import gms.cuit.entity.Gms_User;
import gms.cuit.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UsernamePasswordUtilsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if(userid==null){
            throw new UsernameNotFoundException("用户id不存在!");
        }
        Gms_User user = userMapper.finUserById(userid);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在!");
        }

        //添加权限
        authorities.add(new SimpleGrantedAuthority("ROLE_user"));
        return new User(user.getUser_Id(), user.getUser_Password(), authorities);
    }

    //获取用户 下订单的时候需要获取user对象，如果只是用户名这里可以不用请求数据库
    public Gms_User getSession(){
        String userid = SecurityContextHolder.getContext().getAuthentication().getName();
        Gms_User user = userMapper.finUserById(userid);
        return user;
    }
}
