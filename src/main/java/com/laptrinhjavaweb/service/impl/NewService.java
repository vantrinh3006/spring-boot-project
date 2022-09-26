package com.laptrinhjavaweb.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.converter.NewConverter;
import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.entity.NewEntity;
import com.laptrinhjavaweb.repository.CategoryRepository;
import com.laptrinhjavaweb.repository.NewRepository;
import com.laptrinhjavaweb.service.INewService;

@Service
public class NewService implements INewService {

    @Autowired
    private NewRepository newRepository;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NewConverter newConverter;

    @Override
    public NewDTO save(NewDTO newDTO) throws Exception {
        NewEntity newEntity = new NewEntity();
        Optional<NewEntity> oldNewEntity = null;
        if (newDTO.getId() != null) {    //newDTO đã tồn tại nhờ check id
//            NewEntity oldNewEntity = newRepository.findOne(newDTO.getId());   //=> tìm newDTO cũ
            oldNewEntity = newRepository.findById(newDTO.getId());
            newEntity = newConverter.toEntity(newDTO, oldNewEntity);        //convert sang Entity => update
        } else {    // newDTO chưa tồn tại
            newEntity = newConverter.toEntity(newDTO, oldNewEntity);    //=> convert sang entity => save
        }
        CategoryEntity categoryEntity = categoryRepository.findOneByCode(newDTO.getCategoryCode());  //từ code của newDTO truyền vào => tìm ra được categoryEntity
        if (categoryEntity == null) {
            throw new Exception("Category not exsits!");
        }
        newEntity.setCategory(categoryEntity);    //  sau khi tìm đc category => set vào newEntity
        newEntity = newRepository.save(newEntity);        // dừng newRepository được autowired  để lưu newEntity vào DB
        return newConverter.toDTO(newEntity);        // trả về DTO
    }

    @Override
    public void delete(long[] ids) {

        for(long item: ids) {
            newRepository.deleteById(item);
        }
    }

    @Override
    public List<NewDTO> findAll(Pageable pageable) {
        List<NewDTO> results = new ArrayList<>();
        List<NewEntity> entities = newRepository.findAll(pageable).getContent();
        for (NewEntity item: entities) {
            NewDTO newDTO = newConverter.toDTO(item);
            results.add(newDTO);
        }
        return results;
    }

    @Override
    public int totalItem() {
        return (int) newRepository.count();
    }

    @Override
    public List<NewDTO> findAll() {
        List<NewDTO> results = new ArrayList<>();
        List<NewEntity> entities = newRepository.findAll();
        for (NewEntity item: entities) {
            NewDTO newDTO = newConverter.toDTO(item);
            results.add(newDTO);
        }
        return results;
    }
}
