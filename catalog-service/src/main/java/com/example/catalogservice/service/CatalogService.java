package com.example.catalogservice.service;

import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.dto.CatalogResDto;

import java.util.List;

public interface CatalogService {
    List<CatalogResDto> getAllCatalogs();
}
