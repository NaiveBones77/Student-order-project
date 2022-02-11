package edu.studentorder.dao;

import edu.studentorder.config.Config;
import edu.studentorder.domain.*;
import edu.studentorder.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDaoImpl implements StudentOrderDao {

    private static final String INSERT_DATA = "INSERT INTO public.jc_student_order(" +
            " student_order_status, student_order_date, h_sur_name," +
            "h_given_name, h_patronymic, h_date_of_birth, h_passport_seria," +
            "h_passport_number, h_passport_date, h_passport_office_id, h_post_index," +
            "h_street_code, h_building, h_extension, h_appartment, h_university_id," +
            "h_student_number, w_sur_name, w_given_name, " +
            "w_patronymic, w_date_of_birth, w_passport_seria, w_passport_number, w_passport_date," +
            "w_passport_office_id, w_post_index, w_street_code, w_building, w_extension, w_appartment," +
            "w_university_id, w_student_number, certificate_id, register_office_id, marriage_date)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
            ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_CHILD = "INSERT INTO public.jc_student_child(" +
            "student_order_id, c_sur_name, c_given_name," +
            "c_patronymic, c_date_of_birth, c_certificate_number, c_certificate_date," +
            "c_register_office_id, c_post_index, c_street_code," +
            "c_building, c_extension, c_appartment)" +
            "VALUES (?, ?, ?, ?, " +
            "?, ?, ?, ?, ?," +
            "?, ?, ?, ?);";

    public static final String SELECT_ORDERS =
            "select ro.r_office_area_id, ro.r_office_name, so.*," +
                    "po_h.p_office_area_id as h_p_office_area_id, " +
                    "po_h.p_office_name as h_p_office_name, " +
                    "po_w.p_office_area_id as w_p_office_area_id, " +
                    "po_w.p_office_name as w_p_office_name " +
                    "FROM public.jc_student_order so " +
                    "inner join jc_register_office ro on ro.r_office_id = so.register_office_id " +
                    "inner join jc_passport_office po_h on po_h.p_office_id = so.h_passport_office_id " +
                    "inner join jc_passport_office po_w on po_w.p_office_id = so.w_passport_office_id " +
                    "where student_order_status = ? order by student_order_date LIMIT ?";

    public static final String SELECT_CHILDS =
            "select soc.*, ro.r_office_area_id, ro.r_office_name " +
                    "from jc_student_child soc " +
                    "inner join jc_register_office ro on ro.r_office_id = soc.c_register_office_id " +
                    "where soc.student_order_id in ";


    public static final String SELECT_ORDERS_FULL =
            "select ro.r_office_area_id, ro.r_office_name, so.*," +
                    "po_h.p_office_area_id as h_p_office_area_id, " +
                    "po_h.p_office_name as h_p_office_name, " +
                    "po_w.p_office_area_id as w_p_office_area_id, " +
                    "po_w.p_office_name as w_p_office_name, " +
                    "soc.*, ro_c.r_office_area_id, ro_c.r_office_name " +
                    "FROM public.jc_student_order so " +
                    "inner join jc_register_office ro on ro.r_office_id = so.register_office_id " +
                    "inner join jc_passport_office po_h on po_h.p_office_id = so.h_passport_office_id " +
                    "inner join jc_passport_office po_w on po_w.p_office_id = so.w_passport_office_id " +
                    "inner join jc_student_child soc on soc.student_order_id = so.student_order_id " +
                    "inner join jc_register_office ro_c on ro_c.r_office_id = soc.c_register_office_id " +
                    "where student_order_status = ? order by student_order_date LIMIT ?";
    
    //TODO refactoring - make one method
    private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();
    }

    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {

        Long result = -1L;

        try (Connection con = getConnection();
             PreparedStatement stm = con.prepareStatement(INSERT_DATA,
                     new String[] {"student_order_id"})) {

            con.setAutoCommit(false);

            try {
                //Header
                stm.setInt(1, StudentOrderStatus.START.ordinal());
                stm.setTimestamp(2, Timestamp.valueOf(
                        LocalDateTime.now()
                ));


                //Husband
                int start = 3;
                Adult husband = so.getHusband();
                setParamsForAdult(stm, start, husband);

                //Wife
                start = 18;
                Adult wife = so.getWife();
                setParamsForAdult(stm, start, wife);

                //Marriage
                stm.setString(33, so.getMarriageSertificateId());
                stm.setLong(34, so.getMarriageOffice().getOfficeId());
                stm.setDate(35, Date.valueOf(so.getMarriageDate()));

                stm.executeUpdate();

                ResultSet gkRs = stm.getGeneratedKeys();
                if (gkRs.next())
                {
                    result = gkRs.getLong(1);
                }

                saveChildren(con, so, result);

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }


    private void saveChildren(Connection con, StudentOrder so, Long soId) throws SQLException {
        try (PreparedStatement stm = con.prepareStatement(INSERT_CHILD)){
            for (Child child:so.getChildren())
            {
                stm.setLong(1, soId);
                setParamsForChild(stm, child);
                stm.addBatch();
            }
            stm.executeBatch();
        }
    }


    private void setParamsForAdult(PreparedStatement stm, int start, Adult adult) throws SQLException {
        setParamsForPerson(stm, start, adult);
        stm.setString(start+4, adult.getPassportSeria());
        stm.setString(start+5, adult.getPassportNumber());
        stm.setDate(start+6, Date.valueOf(adult.getIssueDate()));
        stm.setLong(start+7, adult.getIssueDepartment().getOfficeId());
        // hus adress
        setParamsForAdress(stm, start+8, adult);
        stm.setLong(start + 13, adult.getUniversity().getUniversityId());
        stm.setString(start+14, adult.getStudentID());
    }

    private void setParamsForAdress(PreparedStatement stm, int start, Person person) throws SQLException {
        Adress h_adress = person.getAdress();
        stm.setString(start, h_adress.getPostCode());
        stm.setLong(start+1, h_adress.getStreet().getStreet_code());
        stm.setString(start+2, h_adress.getBuilding());
        stm.setString(start+3, h_adress.getExtension());
        stm.setString(start+4, h_adress.getAppartment());
    }

    private void setParamsForPerson(PreparedStatement stm, int start, Person person) throws SQLException {
        stm.setString(start, person.getSurName());
        stm.setString(start+1, person.getGivenName());
        stm.setString(start+2, person.getPatronymic());
        stm.setDate(start+3, Date.valueOf(person.getDateOfBirth()));
    }

    private void setParamsForChild(PreparedStatement stm, Child child) throws SQLException {
        setParamsForPerson(stm, 2, child);
        stm.setString(6, child.getSetrificateNumber());
        stm.setDate(7, java.sql.Date.valueOf(child.getIssueDate()));
        stm.setLong(8, child.getIssueDepartment().getOfficeId());
        setParamsForAdress(stm, 9, child);
    }


    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        return getStudentOrdersOneSelect();
       // return getStudentOrdersTwoSelect();
    }

    private List<StudentOrder> getStudentOrdersOneSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try (Connection con = getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_ORDERS_FULL)) {

            Map<Long, StudentOrder> maps = new HashMap<>();

            stm.setInt(1, StudentOrderStatus.START.ordinal());

            int limit = Integer.parseInt(Config.getProperty(Config.DB_LIMIT));
            stm.setInt(2, limit);
            ResultSet rs;
            rs = stm.executeQuery();

            int counter = 0;
            while (rs.next())
            {
                Long soId = rs.getLong("student_order_id");
                if (!maps.containsKey(soId)) {
                    StudentOrder so = getFullStudentOrder(rs);

                    result.add(so);
                    maps.put(soId, so);
                }

                StudentOrder so = maps.get(soId);
                so.addChild(fillChild(rs));
                counter++;
            }

            if (counter >= limit)
            {
                result.remove(result.size()-1);
            }
            rs.close();

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;
    }


    private List<StudentOrder> getStudentOrdersTwoSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try (Connection con = getConnection();
             PreparedStatement stm = con.prepareStatement(SELECT_ORDERS)) {

            stm.setInt(1, StudentOrderStatus.START.ordinal());
            stm.setInt(2, Integer.parseInt(Config.getProperty(Config.DB_LIMIT)));
            ResultSet rs;
            rs = stm.executeQuery();

            while (rs.next())
            {
                StudentOrder so = getFullStudentOrder(rs);

                result.add(so);

            }
            findChildren(con, result);

            rs.close();

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;
    }

    private StudentOrder getFullStudentOrder(ResultSet rs) throws SQLException {
        StudentOrder so = new StudentOrder();

        fillStudentOrder(rs, so);
        fillMarriage(rs, so);

        Adult husband = fillAdult(rs, "h_");
        Adult wife = fillAdult(rs, "w_");
        so.setHusband(husband);
        so.setWife(wife);
        return so;
    }

    private void findChildren(Connection con, List<StudentOrder> result) throws SQLException {
        String cl ="(" +
                result.stream().map(so -> String.valueOf(so.getStudentOrderId()))
                .collect(Collectors.joining(","))
                + ")";

        Map<Long, StudentOrder> maps = result.stream().collect(Collectors
                .toMap(so -> so.getStudentOrderId(), so -> so));

        try (PreparedStatement stm = con.prepareStatement(SELECT_CHILDS + cl)){
            ResultSet rs = stm.executeQuery();
            while (rs.next())
            {
                Child child = fillChild(rs);
                StudentOrder so = maps.get(rs.getLong("student_order_id"));
                so.addChild(child);
            }
        }
    }



    private Adult fillAdult(ResultSet rs, String pref) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(rs.getString(pref + "sur_name"));
        adult.setGivenName(rs.getString(pref + "given_name"));
        adult.setPatronymic(rs.getString(pref + "patronymic"));
        adult.setDateOfBirth(rs.getDate(pref + "date_of_birth").toLocalDate());
        adult.setPassportSeria(rs.getString(pref + "passport_seria"));
        adult.setPassportNumber(rs.getString(pref + "passport_number"));
        adult.setIssueDate(rs.getDate(pref + "passport_date").toLocalDate());

        Long poId = rs.getLong(pref + "passport_office_id");
        String poArea = rs.getString(pref + "p_office_area_id");
        String poName = rs.getString(pref + "p_office_name");

        PassportOffice passportOffice = new PassportOffice(poId, poArea, poName);
        adult.setIssueDepartment(passportOffice);

        Adress adress = new Adress();
        Street street = new Street(rs.getLong(pref + "street_code"), "");
        adress.setPostCode(rs.getString(pref + "post_index"));
        adress.setBuilding(rs.getString(pref + "building"));
        adress.setExtension(rs.getString(pref + "extension"));
        adress.setAppartment(rs.getString(pref + "appartment"));
        adress.setStreet(street);
        adult.setAdress(adress);

        University university = new University(rs
                .getLong(pref + "university_id"), "");
        adult.setUniversity(university);
        adult.setStudentID(rs.getString(pref + "student_number"));


        return adult;
    }


    private void fillStudentOrder(ResultSet rs, StudentOrder so) throws SQLException {
        so.setStudentOrderId(rs.getLong("student_order_id"));
        so.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());
        so.setStudentOrderStatus(StudentOrderStatus
                .fromValue(rs.getInt("student_order_status")));
    }

    private void fillMarriage(ResultSet rs, StudentOrder so) throws SQLException {
        so.setMarriageSertificateId(rs.getString("certificate_id"));
        so.setMarriageDate(rs.getDate("marriage_date").toLocalDate());

        Long roId = rs.getLong("register_office_id");
        String areaId = rs.getString("r_office_area_id");
        String areaName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, areaId, areaName);
        so.setMarriageOffice(ro);
    }

    private Child fillChild(ResultSet rs) throws SQLException {
        String surName = rs.getString("c_sur_name");
        String givenName = rs.getString("c_given_name");
        String patronymic = rs.getString("c_patronymic");
        LocalDate birthday = rs.getDate("c_date_of_birth").toLocalDate();

        Child child = new Child(surName, givenName, patronymic, birthday);

        child.setSetrificateNumber(rs.getString("c_certificate_number"));
        child.setIssueDate(rs.getDate("c_certificate_date").toLocalDate());


        Long roId = rs.getLong("c_register_office_id");
        String roArea = rs.getString("r_office_area_id");
        String roName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, roArea, roName);
        child.setIssueDepartment(ro);

        Adress adress = new Adress();
        Street street = new Street(rs.getLong("c_street_code"), "");
        adress.setPostCode(rs.getString("c_post_index"));
        adress.setBuilding(rs.getString("c_building"));
        adress.setExtension(rs.getString("c_extension"));
        adress.setAppartment(rs.getString("c_appartment"));
        adress.setStreet(street);
        child.setAdress(adress);

        return child;
    }
}
