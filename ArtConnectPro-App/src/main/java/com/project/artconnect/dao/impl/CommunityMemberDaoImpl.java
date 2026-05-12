package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import java.util.List;
import java.util.Optional;

public class CommunityMemberDaoImpl implements CommunityMemberDao {
    private final JdbcCommunityMemberDao jdbcDao = new JdbcCommunityMemberDao();

    @Override
    public Optional<CommunityMember> findById(Long id) {
        return jdbcDao.findById(id);
    }

    @Override
    public List<CommunityMember> findAll() {
        return jdbcDao.findAll();
    }
}