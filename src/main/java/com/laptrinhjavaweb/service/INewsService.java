package com.laptrinhjavaweb.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.laptrinhjavaweb.dto.NewsDTO;

//@Service
public interface INewsService {
    NewsDTO save(NewsDTO newsDTO) throws Exception;
    void delete(long[] ids);
    List<NewsDTO> findAll(Pageable pageable);
    List<NewsDTO> findAll();

    int totalItem();
}
