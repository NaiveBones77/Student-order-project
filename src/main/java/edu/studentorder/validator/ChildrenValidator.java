package edu.studentorder.validator;

import edu.studentorder.domain.AnswerChildren;
import edu.studentorder.domain.StudentOrder;

public class ChildrenValidator {
    public String hostName;

    public AnswerChildren checkChildren(StudentOrder so)
    {
        System.out.println("Checking children " + hostName);
        AnswerChildren ans = new AnswerChildren();
        return ans;
    }
}
