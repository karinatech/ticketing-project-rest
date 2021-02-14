package com.cybertek.implementation;

import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import com.cybertek.entity.common.UserPrincipal;
import com.cybertek.mapper.MapperUtil;
import com.cybertek.repository.UserRepository;
import com.cybertek.service.SecurityService;
import com.cybertek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {

private UserService userService;
private MapperUtil mapperUtil;

    public SecurityServiceImpl(UserService userService, MapperUtil mapperUtil) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserDTO user = userService.findByUserName(s);

        if(user==null){
            throw new UsernameNotFoundException("This user does not exists");
        }

        return new org.springframework.security.core.userdetails.User(user.getId().toString(),user.getPassWord(), listGrantedAuthority(user));
    }

    @Override
    public User loadUser(String user) {
     UserDTO userDTO= userService.findByUserName(user);
     return mapperUtil.convert(userDTO,new User());
    }
    private Collection<? extends GrantedAuthority>listGrantedAuthority(UserDTO userDTO){
        List<GrantedAuthority>authorityList=new ArrayList<>();
        GrantedAuthority authority=new SimpleGrantedAuthority(userDTO.getRole().getDescription());
        authorityList.add(authority);
        return authorityList;
    }


}
