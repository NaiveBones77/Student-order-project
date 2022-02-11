package edu.studentorder.dao;

import edu.studentorder.config.Config;
import edu.studentorder.domain.CountryArea;
import edu.studentorder.domain.PassportOffice;
import edu.studentorder.domain.RegisterOffice;
import edu.studentorder.domain.Street;
import edu.studentorder.exception.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DictionaryDaoImpl implements DictionaryDao {

    private static final String GET_STREET = "select street_code, street_name from jc_street" +
            " where upper(street_name) like upper(?)";

    private static final String GET_PASSPORT = "select * from jc_passport_office" +
            " where p_office_area_id = ?";

    private static final String GET_REGISTER = "SELECT * " +
            "FROM jc_register_office WHERE r_office_area_id = ?";

    public static final String GET_AREA = "select * from jc_country_struct" +
            " where area_id like ? and area_id <> ?";

    private Connection getConnection() throws SQLException {
      return ConnectionBuilder.getConnection();
    }

    public List<Street> findedStreets(String pattern) throws DaoException
    {
        List<Street> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stm = con.prepareStatement(GET_STREET)) {

            stm.setString(1, "%" + pattern + "%");
            ResultSet set = stm.executeQuery();
            while (set.next())
            {
                Street street = new Street(set.getLong("street_code"), set.getString("street_name"));
                result.add(street);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    @Override
    public List<PassportOffice> findedPassportOffices(String areaId) throws DaoException {
        List<PassportOffice> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stm = con.prepareStatement(GET_PASSPORT)) {

            stm.setString(1,  areaId );
            ResultSet set = stm.executeQuery();
            while (set.next())
            {
                PassportOffice pOffice = new PassportOffice(set.getLong("p_office_id"),
                        set.getString("p_office_area_id"),
                        set.getString("p_office_name"));
                result.add(pOffice);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    @Override
    public List<RegisterOffice> findedRegisterOffices(String areaId) throws DaoException {
        List<RegisterOffice> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stm = con.prepareStatement(GET_REGISTER)) {

            stm.setString(1, areaId);
            ResultSet set = stm.executeQuery();
            while (set.next())
            {
                RegisterOffice rOffice = new RegisterOffice(set.getLong("r_office_id"),
                        set.getString("r_office_area_id"),
                        set.getString("r_office_name"));
                result.add(rOffice);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    @Override
    public List<CountryArea> findedAreas(String areaId) throws DaoException {
        List<CountryArea> result = new LinkedList<>();


        try (Connection con = getConnection();
             PreparedStatement stm = con.prepareStatement(GET_AREA)) {

            String param1 = buildParam(areaId);
            String param2 = areaId;
            stm.setString(1, param1);
            stm.setString(2, param2);
            ResultSet set = stm.executeQuery();
            while (set.next())
            {
                CountryArea area = new CountryArea(set.getString("area_id"),
                        set.getString("area_name"));
                result.add(area);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    private String buildParam(String areaId) throws SQLException {
        if (areaId == null || areaId.trim().isEmpty())
        {
            return "__0000000000";
        }
        else if (areaId.endsWith("0000000000"))
        {
            return areaId.substring(0, 2) + "___0000000";
        }
        else if (areaId.endsWith("0000000"))
        {
            return areaId.substring(0, 5) + "___0000";
        }
        else if (areaId.endsWith("0000"))
        {
            return areaId.substring(0, 8) + "____";
        }
        throw new SQLException("Invalid area_id parametr:" + areaId);
    }


}
