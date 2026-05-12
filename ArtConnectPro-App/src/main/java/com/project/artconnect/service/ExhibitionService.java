package com.project.artconnect.service;

import com.project.artconnect.model.Exhibition;
import java.util.List;

public interface ExhibitionService {
    List<Exhibition> getAllExhibitions();
    List<Exhibition> getUpcomingExhibitions();
}