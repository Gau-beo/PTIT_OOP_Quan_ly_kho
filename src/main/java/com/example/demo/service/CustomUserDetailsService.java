package com.example.demo.service;

import com.example.demo.entity.NguoiDung;
import com.example.demo.repository.NguoiDungRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final NguoiDungRepository repository;

    public CustomUserDetailsService(
            NguoiDungRepository repository) {

        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        NguoiDung nguoiDung =
                repository.findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Không tìm thấy tài khoản: "
                                                + email
                                )
                        );

        return new User(
                nguoiDung.getEmail(),
                nguoiDung.getMatKhau(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(
                                "ROLE_"
                                        + nguoiDung.getVaiTro()
                        )
                )
        );
    }
}