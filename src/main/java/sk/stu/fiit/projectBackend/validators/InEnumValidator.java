/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.validators;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Adam Bublavý
 */
public class InEnumValidator implements
        ConstraintValidator<InEnum, Enum> {

    private List<String> validValues;

    @Override
    public void initialize(InEnum constraintAnnotation) {
        this.validValues = Stream.of(constraintAnnotation.enumClass().
                getEnumConstants()).map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        return value == null || validValues.contains(value.name());
    }

}
