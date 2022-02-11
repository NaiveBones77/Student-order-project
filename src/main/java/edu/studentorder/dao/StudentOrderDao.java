package edu.studentorder.dao;

import edu.studentorder.domain.StudentOrder;
import edu.studentorder.exception.DaoException;

import java.util.List;

public interface StudentOrderDao {

    Long saveStudentOrder(StudentOrder so) throws DaoException;
    List<StudentOrder> getStudentOrders() throws DaoException;
}
