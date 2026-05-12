package com.project.artconnect.service.impl;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.dao.impl.WorkshopDaoImpl;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Booking;
import com.project.artconnect.service.WorkshopService;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class WorkshopServiceImpl implements WorkshopService {
    private final WorkshopDao workshopDao = new WorkshopDaoImpl();

    @Override
    public List<Workshop> getAllWorkshops() {
        return workshopDao.findAll();
    }

    @Override
    public Optional<Workshop> getWorkshopByTitle(String title) {
        return workshopDao.findAll().stream()
                .filter(w -> w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        System.out.println("Réservation effectuée pour " + member.getName());
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        return new ArrayList<>();
    }
}