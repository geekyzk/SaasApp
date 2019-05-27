package com.em248.security;


import cn.hutool.core.bean.BeanUtil;
import com.em248.exception.BackException;
import com.em248.model.User;
import com.em248.repository.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {



    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository.findByEmail(lowercaseLogin)
                .map(user -> createSpringSecurityUser(lowercaseLogin, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

    }

    private CustomUserDetail createSpringSecurityUser(String lowercaseLogin, User user) {

        Set<SimpleGrantedAuthority> grantedAuthorities = Collections.EMPTY_SET;
        CustomUserDetail customUserDetail = new CustomUserDetail(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getDataSourceConfig().getId(),
                grantedAuthorities,
                true,
                true,
                true,
                true);

        return customUserDetail;
    }

}
