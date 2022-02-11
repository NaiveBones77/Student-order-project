package edu.studentorder.validator;

import edu.studentorder.domain.AnswerMarriage;
import edu.studentorder.domain.StudentOrder;

public class MarriageValidator {
    public String hostName;

    public AnswerMarriage checkMarriage(StudentOrder so)
    {
        System.out.println("Checking marriage " + hostName);
        AnswerMarriage ans = new AnswerMarriage();
        return ans;
    }
}
