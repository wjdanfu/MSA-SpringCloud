package com.example.catalogservice.service;

import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.domain.CatalogRepository;
import com.example.catalogservice.dto.CatalogResDto;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService{
    CatalogRepository catalogRepository;

    @Autowired
    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public List<CatalogResDto> getAllCatalogs() {
        Iterable<Catalog> entity = catalogRepository.findAll();
        List<CatalogResDto> catalogResDtos = new ArrayList<>();
        List<Catalog> myList = Lists.newArrayList(entity);

        for (int i = 0; i<myList.size(); i++){
            CatalogResDto catalogResDto = new CatalogResDto(myList.get(i));
            catalogResDtos.add(catalogResDto);
        }

        return catalogResDtos;
    }

}
