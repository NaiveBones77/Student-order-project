package edu.studentorder.validator.register;

import edu.studentorder.domain.Person;
import edu.studentorder.domain.register.CityRegisterResponse;
import edu.studentorder.exception.CityRegisterException;
import edu.studentorder.exception.TransportException;

public interface CityRegisterChecker {
    CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException    ;
}
