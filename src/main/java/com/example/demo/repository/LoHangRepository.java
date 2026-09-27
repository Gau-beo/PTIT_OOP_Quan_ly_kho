package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.LoHang;

public interface LoHangRepository extends JpaRepository<LoHang, Long> {
    List<LoHang> findByMaLoHangContaining(String maLoHang);
}
