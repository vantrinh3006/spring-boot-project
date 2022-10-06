package com.laptrinhjavaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.laptrinhjavaweb.controller.output.NewsOutput;
import com.laptrinhjavaweb.dto.NewsDTO;
import com.laptrinhjavaweb.service.INewsService;

@CrossOrigin
@RestController
@RequestMapping("/vn")
public class NewsController {
    @Autowired
    private INewsService newsService;

    //500error if select all
//	public NewsOutput showNews(@RequestParam("page") int page,
//	 @RequestParam("limit") int limit) {

    //200ok
//	public NewsOutput showNews(@RequestParam("page") Integer page,
//			 @RequestParam("limit") Integer limit) {
    @GetMapping(value = "/news")
    public ResponseEntity<NewsOutput> showNews(@RequestParam(value = "page" , required = false) Integer page,
                                               @RequestParam(value = "limit" , required = false) Integer limit) {
        NewsOutput result = new NewsOutput();
        if(page != null && limit != null) {
            result.setPage(page);
            Pageable pageable =  PageRequest.of(page - 1,limit);
            result.setListResult(newsService.findAll(pageable));
            result.setTotalPage((int)(Math.ceil((double) (newsService.totalItem()) / limit)));
        }
        else {
            result.setListResult(newsService.findAll());
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(result);
    }

    @PostMapping(value = "/news")
    public ResponseEntity<?> createNews(@RequestBody NewsDTO model) {
        try {
            newsService.save(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(newsService.save(model));

        }catch (Exception e) {
            return 	ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PutMapping(value = "/news/{id}")
    public ResponseEntity<?> updateNew(@RequestBody NewsDTO model, @PathVariable("id") long id) {
        model.setId(id);
        System.out.println(model);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(newsService.save(model));
        }catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

//	@PostMapping(value = "/news")
//	public NewsDTO createNew(@RequestBody NewsDTO model) {
//		return newsService.save(model);
//	}
//
//	@PutMapping(value = "/news/{id}")
//	public NewsDTO updateNews(@RequestBody NewsDTO model, @PathVariable("id") long id) {
//		model.setId(id);
//		return newsService.save(model);
//	}

    @DeleteMapping(value = "/news")
    public void deleteNews(@RequestBody long[] ids) {
        newsService.delete(ids);
    }

}
