package org.ssu.standings.dao.entity;

import org.junit.Test;

import static org.junit.Assert.*;

public class UniversityDAOTest {
    @Test
    public void cloneTest() throws Exception {
        UniversityDAO universityDAO = new UniversityDAO.Builder().withId(1L).withName("name").withRegion("region").withType("type").build();
        UniversityDAO copy = universityDAO.clone();

        assertNotSame(universityDAO, copy);
    }

}