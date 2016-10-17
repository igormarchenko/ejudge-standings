package org.ssu.standings.service;

import org.springframework.stereotype.Service;
import org.ssu.standings.repository.PropertiesRepository;

import javax.annotation.Resource;

@Service
public class PropertiesService {
    @Resource
    private PropertiesRepository propertiesRepository;

    public String getLogin() {
        return propertiesRepository.findByKey("login").getValue();
    }

    public String getPassword() {
        return propertiesRepository.findByKey("password").getValue();
    }
}
