package com.laptrinhjavaweb.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.laptrinhjavaweb.dto.NewDTO;
import org.springframework.stereotype.Service;

//@Service
public interface INewService {
    NewDTO save(NewDTO newDTO) throws Exception;
    void delete(long[] ids);
    List<NewDTO> findAll(Pageable pageable);
    List<NewDTO> findAll();

    int totalItem();
}
