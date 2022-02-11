package edu.studentorder.dao;

import edu.studentorder.domain.CountryArea;
import edu.studentorder.domain.PassportOffice;
import edu.studentorder.domain.RegisterOffice;
import edu.studentorder.domain.Street;
import edu.studentorder.exception.DaoException;

import java.util.List;

public interface DictionaryDao {

    List<Street> findedStreets(String pattern) throws DaoException;
    List<PassportOffice> findedPassportOffices(String areaId) throws DaoException;
    List<RegisterOffice> findedRegisterOffices(String areaId) throws DaoException;
    List<CountryArea> findedAreas(String areaId) throws DaoException;

}
