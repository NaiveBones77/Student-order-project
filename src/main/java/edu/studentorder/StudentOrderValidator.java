package edu.studentorder;

import edu.studentorder.dao.StudentDaoImpl;
import edu.studentorder.dao.StudentOrderDao;
import edu.studentorder.domain.*;
import edu.studentorder.domain.register.AnwerCityRegister;
import edu.studentorder.exception.DaoException;
import edu.studentorder.mail.MailSender;
import edu.studentorder.validator.ChildrenValidator;
import edu.studentorder.validator.CityRegisterValidator;
import edu.studentorder.validator.MarriageValidator;
import edu.studentorder.validator.StudentValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderValidator {

    private CityRegisterValidator cityRegisterVal;
    private MarriageValidator marriageVal;
    private ChildrenValidator childrenVal;
    private StudentValidator studentVal;
    private MailSender mailSender;

    public StudentOrderValidator (){
        cityRegisterVal = new CityRegisterValidator();
        marriageVal = new MarriageValidator();
        childrenVal = new ChildrenValidator();
        studentVal = new StudentValidator();
        mailSender = new MailSender();
    }

    static public void main(String[] args) {
        StudentOrderValidator sov = new StudentOrderValidator();
        sov.checkAll();
    }

    public void checkAll()
    {
        try {
            List<StudentOrder> soList = readStudentOrders();

//        for (int i = 0; i < soList.length; i++) {
//            System.out.println();
//            checkOneOrder(soList[i]);
//        }

            for (StudentOrder so: soList) {
                System.out.println();
                checkOneOrder(so);
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }


    }

    public void checkOneOrder(StudentOrder so)
    {
        AnwerCityRegister cityAnswer = checkCityRegiter(so);
        AnswerMarriage marriageAnswer = checkMarriage(so);
        AnswerChildren childrenAnswer = checkChildren(so);
        AnswerStudent studentAnswer = checkStudent(so);
        sendMail(so);
    }

    public AnwerCityRegister checkCityRegiter(StudentOrder so)
    {
        return cityRegisterVal.checkCityRegiter(so);
    }

    public AnswerMarriage checkMarriage(StudentOrder so)
    {

        return  marriageVal.checkMarriage(so);
    }

    public AnswerChildren checkChildren(StudentOrder so)
    {

        return childrenVal.checkChildren(so);
    }

    public AnswerStudent checkStudent(StudentOrder so)
    {

        return studentVal.checkStudent(so);
    }

    public List<StudentOrder> readStudentOrders() throws DaoException {
        return new StudentDaoImpl().getStudentOrders();
    }

    public void sendMail(StudentOrder so)
    {
        mailSender.sendMail(so);
    }
}
