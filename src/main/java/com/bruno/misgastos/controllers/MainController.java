package com.bruno.misgastos.controllers;

import com.bruno.misgastos.dto.*;
import com.bruno.misgastos.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = mainService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/subcategories")
    public ResponseEntity<List<Subcategory>> getSubcategories() {
        List<Subcategory> subcategories = mainService.getSubcategories();
        return new ResponseEntity<>(subcategories, HttpStatus.OK);
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getGroups() {
        List<Group> groups = mainService.getGroups();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> groups = mainService.getAccounts();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/spends")
    public ResponseEntity<List<Spend>> getSpends() {
        List<Spend> spends = mainService.getSpends();
        return new ResponseEntity<>(spends, HttpStatus.OK);
    }
}
