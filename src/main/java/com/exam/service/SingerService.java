package com.exam.service;

import java.util.List;

import com.exam.entity.Singer;

public interface SingerService {
	List<Singer> findAll();
    List<Singer> findAllWithAlbum();
    Singer findById(Long id);
    Singer save(Singer singer);
    void delete(Singer singer);
    List<Singer> findAllByNativeQuery();
}
