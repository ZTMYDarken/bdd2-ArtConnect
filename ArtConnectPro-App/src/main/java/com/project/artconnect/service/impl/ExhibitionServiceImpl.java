package com.project.artconnect.service.impl;

import com.project.artconnect.dao.impl.ExhibitionDaoImpl;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.service.ExhibitionService;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class ExhibitionServiceImpl implements ExhibitionService {
    private final ExhibitionDaoImpl exhibitionDao = new ExhibitionDaoImpl();

    @Override
    public List<Exhibition> getAllExhibitions() {
        return exhibitionDao.findAll();
    }

    @Override
    public List<Exhibition> getUpcomingExhibitions() {
        java.time.LocalDate now = java.time.LocalDate.now();
        return exhibitionDao.findAll().stream()
                .filter(e -> e.getEndDate() != null && e.getEndDate().isAfter(now))
                .collect(Collectors.toList());
    }
}