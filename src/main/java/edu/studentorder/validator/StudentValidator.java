package edu.studentorder.validator;

import edu.studentorder.domain.AnswerStudent;
import edu.studentorder.domain.StudentOrder;

public class StudentValidator {
    public String hostName;

    public AnswerStudent checkStudent(StudentOrder so)
    {
        System.out.println("Checking students " + hostName);
        AnswerStudent ans = new AnswerStudent();
        return ans;
    }
}
