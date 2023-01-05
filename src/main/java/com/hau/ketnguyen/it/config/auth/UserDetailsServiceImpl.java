package com.hau.ketnguyen.it.config.auth;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findByUsernameAndStatus(username, User.Status.ACTIVE);

        if (userOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Tài khoản đã bị xóa hoặc khóa, vui lòng liên hệ quản trị viên!");
        }

        User user = userOptional.get();

        List<GrantedAuthority> authorites = user.getRoles()
                .stream().map(role -> {
                    if (role != null) {
                        return new SimpleGrantedAuthority(role.getCode());
                    }
                    return null;
                }).collect(Collectors.toList());

        CustomUser customerUser = new CustomUser(username, user.getPassword(), true, true,
                true,true, authorites);
        customerUser.setId(user.getId());
        return customerUser;
    }
}
