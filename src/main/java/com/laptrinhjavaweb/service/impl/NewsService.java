package com.laptrinhjavaweb.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.converter.NewsConverter;
import com.laptrinhjavaweb.dto.NewsDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.entity.NewsEntity;
import com.laptrinhjavaweb.repository.CategoryRepository;
import com.laptrinhjavaweb.repository.NewsRepository;
import com.laptrinhjavaweb.service.INewsService;

@Service
public class NewsService implements INewsService {

    @Autowired
    private NewsRepository newsRepository;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NewsConverter newsConverter;

    @Override
    public NewsDTO save(NewsDTO newsDTO) throws Exception {
        NewsEntity newsEntity = new NewsEntity();
        Optional<NewsEntity> oldNewsEntity = null;
        if (newsDTO.getId() != null) {    //newsDTO đã tồn tại nhờ check id
//            NewsEntity oldNewsEntity = newsRepository.findOne(newsDTO.getId());   //=> tìm newsDTO cũ
            oldNewsEntity = newsRepository.findById(newsDTO.getId());
            newsEntity = newsConverter.toEntity(newsDTO, oldNewsEntity);        //convert sang Entity => update
        } else {    // newDTO chưa tồn tại
            newsEntity = newsConverter.toEntity(newsDTO, oldNewsEntity);    //=> convert sang entity => save
        }
        CategoryEntity categoryEntity = categoryRepository.findOneByCode(newsDTO.getCategoryCode());  //từ code của newsDTO truyền vào => tìm ra được categoryEntity
        if (categoryEntity == null) {
            throw new Exception("Category not exsits!");
        }
        newsEntity.setCategory(categoryEntity);    //  sau khi tìm đc category => set vào newsEntity
        newsEntity = newsRepository.save(newsEntity);        // dừng newsRepository được autowired  để lưu newsEntity vào DB
        return newsConverter.toDTO(newsEntity);        // trả về DTO
    }

    @Override
    public void delete(long[] ids) {

        for(long item: ids) {
            newsRepository.deleteById(item);
        }
    }

    @Override
    public List<NewsDTO> findAll(Pageable pageable) {
        List<NewsDTO> results = new ArrayList<>();
        List<NewsEntity> entities = newsRepository.findAll(pageable).getContent();
        for (NewsEntity item: entities) {
            NewsDTO newsDTO = newsConverter.toDTO(item);
            results.add(newsDTO);
        }
        return results;
    }

    @Override
    public int totalItem() {
        return (int) newsRepository.count();
    }

    @Override
    public List<NewsDTO> findAll() {
        List<NewsDTO> results = new ArrayList<>();
        List<NewsEntity> entities = newsRepository.findAll();
        for (NewsEntity item: entities) {
            NewsDTO newsDTO = newsConverter.toDTO(item);
            results.add(newsDTO);
        }
        return results;
    }
}
