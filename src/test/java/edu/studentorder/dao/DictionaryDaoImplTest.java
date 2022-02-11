package edu.studentorder.dao;


import edu.studentorder.domain.CountryArea;
import edu.studentorder.domain.PassportOffice;
import edu.studentorder.domain.RegisterOffice;
import edu.studentorder.domain.Street;
import edu.studentorder.exception.DaoException;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DictionaryDaoImplTest {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryDaoImplTest.class);

    @BeforeClass
    public static void StartUp() throws Exception {
        DBInit.resetDB();
    }


    @Test
    public void testStreet() throws DaoException {

        LocalDateTime dt1 = LocalDateTime.now();
        LocalDateTime dt2 = LocalDateTime.now();
        logger.debug("TEST: {} {}", dt1, dt2);
        List<Street> list_streets = new DictionaryDaoImpl().findedStreets("ова");

        Assert.assertTrue(list_streets.size() == 2);

    }

    @Test
    public void testPassportOffice() throws DaoException {
        List<PassportOffice> list_po = new DictionaryDaoImpl().findedPassportOffices("010020000000");

        Assert.assertTrue(list_po.size() == 2);
    }

    @Test
    public void testRegisterOffice() throws DaoException {
        List<RegisterOffice> list_ro = new DictionaryDaoImpl().findedRegisterOffices("010010000000");

        Assert.assertTrue(list_ro.size() == 2);
    }


    @Test
    public void testArea() throws DaoException {
        List<CountryArea> countries = new DictionaryDaoImpl().findedAreas("020010010000");

        for (CountryArea c:countries)
        {
            System.out.println(c.getAreaId() + " " + c.getAreaName());
        }

        Assert.assertTrue(countries.size() == 2);
    }
}