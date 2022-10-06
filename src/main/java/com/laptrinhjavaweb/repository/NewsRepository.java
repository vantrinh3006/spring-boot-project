package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

}
