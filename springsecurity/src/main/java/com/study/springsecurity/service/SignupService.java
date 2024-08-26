package com.study.springsecurity.service;

import com.study.springsecurity.aspect.annotation.TimeAop;
import com.study.springsecurity.domain.entity.Role;
import com.study.springsecurity.domain.entity.User;
import com.study.springsecurity.dto.request.ReqSignupDto;
import com.study.springsecurity.repository.RoleRepository;
import com.study.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class SignupService {

     private final UserRepository userRepository;
     private final RoleRepository roleRepository;
     private final BCryptPasswordEncoder passwordEncoder;

    @TimeAop
    @Transactional(rollbackFor = Exception.class)
    public User signup(ReqSignupDto dto){
        User user = dto.toEntity(passwordEncoder);
        Role role= roleRepository.findByName("ROLE_USER").orElseGet(
                () -> roleRepository.save(Role.builder().name("ROLE_USER").build())
        );

        user.setRoles(Set.of(role));
        user = userRepository.save(user);

//        UserRole userRole = UserRole.builder()
//                .user(user)
//                .role(role)
//                .build();
//        userRole = userRoleRepository.save(userRole);
//        user.setUserRoles(Set.of(userRole));
        return user;
    }

    public boolean isDulicatedUsername(String username) {
        return  userRepository.findByUsername(username).isPresent();
    }

}
