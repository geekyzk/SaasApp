package com.em248.security.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.em248.configuration.ApplicationProperties;
import com.em248.model.User;
import com.em248.repository.UserRepository;
import com.em248.security.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DefaultTokenProvider extends TokenProvider {

    private final UserRepository userRepository;

    public DefaultTokenProvider(ApplicationProperties applicationProperties, UserRepository userRepository) {
        super(applicationProperties);
        this.userRepository = userRepository;
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();
        String[] permissions = claims.get(AUTHORITIES_KEY).toString().split(",");
        Collection<SimpleGrantedAuthority> authorities = Collections.EMPTY_LIST;
        if(permissions.length > 0 && !permissions[0].equals("")) {
            authorities =
                Arrays.stream(permissions)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        Optional<User> optionalUser = userRepository.findById(JSONUtil.parseObj(claims.getSubject()).getStr("userId"));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            CustomUserDetail jwtUser = new CustomUserDetail(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getDataSourceConfig().getId(),
                    Collections.EMPTY_SET,
                    true,
                    true,
                    true,
                    true
            );
            return new UsernamePasswordAuthenticationToken(jwtUser, token, authorities);
        } else {
            return null;
        }
    }
}
