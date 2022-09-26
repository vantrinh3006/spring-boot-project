package com.laptrinhjavaweb.api;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhjavaweb.api.output.NewOutput;
import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.service.INewService;

@CrossOrigin
@RestController
public class NewAPI {
    @Autowired
    private INewService newService;

    @GetMapping(value = "/new")

    //500error if select all
//	public NewOutput showNew(@RequestParam("page") int page,
//	 @RequestParam("limit") int limit) {

    //200ok
//	public NewOutput showNew(@RequestParam("page") Integer page,
//			 @RequestParam("limit") Integer limit) {

    public NewOutput showNew(@RequestParam(value = "page" , required = false) Integer page,
                             @RequestParam(value = "limit" , required = false) Integer limit) {

        NewOutput result = new NewOutput();

        if(page != null && limit != null) {
            result.setPage(page);
//            Pageable pageable = new PageRequest(page - 1, limit);
            Pageable pageable =  PageRequest.of(1,1);
            result.setListResult(newService.findAll(pageable));
            result.setTotalPage(
                    (int)(
                            Math.ceil(
                                    (double) (newService.totalItem()) / limit
                            )
                    )
            );
        }else {
            result.setListResult(newService.findAll());
        }

        return result;
    }

    @PostMapping(value = "/new")
    public ResponseEntity<?> createNew(@RequestBody NewDTO model) {
//		return newService.save(model);
        try {
            return 	ResponseEntity.ok( newService.save(model));
        }catch (Exception e) {
            return 	ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PutMapping(value = "/new/{id}")
    public ResponseEntity<?> updateNew(@RequestBody NewDTO model, @PathVariable("id") long id) {

        model.setId(id);
        try {
            return 	ResponseEntity.ok( newService.save(model));
        }catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }

    }

//	@PostMapping(value = "/new")
//	public NewDTO createNew(@RequestBody NewDTO model) {
//		return newService.save(model);
//	}
//
//	@PutMapping(value = "/new/{id}")
//	public NewDTO updateNew(@RequestBody NewDTO model, @PathVariable("id") long id) {
//		model.setId(id);
//		return newService.save(model);
//	}

    @DeleteMapping(value = "/new")
    public void deleteNew(@RequestBody long[] ids) {
        newService.delete(ids);
    }
}
