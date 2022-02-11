package edu.studentorder.validator;

import edu.studentorder.domain.*;
import edu.studentorder.domain.register.AnswerCityRegisterItem;
import edu.studentorder.domain.register.AnwerCityRegister;
import edu.studentorder.domain.register.CityRegisterResponse;
import edu.studentorder.exception.CityRegisterException;
import edu.studentorder.exception.TransportException;
import edu.studentorder.validator.register.CityRegisterChecker;
import edu.studentorder.validator.register.FakeCityRegisterChecker;

import java.util.List;

public class CityRegisterValidator {

    private static final String IN_CODE = "NO_GRN";
    public String hostName;
    protected int port;

    private CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        personChecker = new FakeCityRegisterChecker();
    }

    public AnwerCityRegister checkCityRegiter(StudentOrder so)
    {
        AnwerCityRegister answer = new AnwerCityRegister();

        answer.addItem(checkPerson(so.getHusband()));
        answer.addItem(checkPerson(so.getWife()));


        List<Child> children = so.getChildren();
        for (Child child: children)
        {
            answer.addItem(checkPerson(child));
        }

        return answer;
    }

    private AnswerCityRegisterItem checkPerson(Person person)
    {

        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;

        try {
           CityRegisterResponse tmp = personChecker.checkPerson(person);
           status = tmp.isExisting() ?
                   AnswerCityRegisterItem.CityStatus.YES :
                   AnswerCityRegisterItem.CityStatus.NO;
        }
        catch (CityRegisterException ex)
        {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(ex.getCode(), ex.getMessage());
        }
        catch (TransportException ex)
        {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError( IN_CODE, ex.getMessage());
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, ex.getMessage());
        }

        AnswerCityRegisterItem ans =
                new AnswerCityRegisterItem(status, person, error);

        return null;
    }
}
