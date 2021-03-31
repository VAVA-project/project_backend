/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Adam Bublavý
 */
public class InValidator implements ConstraintValidator<In, String> {
    
    private String[] allowedValues;
    private boolean ignoreCase;

    @Override
    public void initialize(In constraintAnnotation) {
        allowedValues = constraintAnnotation.allowedValues();
        ignoreCase = constraintAnnotation.ignoreCase();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for(String s : allowedValues) {
            if(ignoreCase) {
                if(s.equalsIgnoreCase(value)) {
                    return true;
                }
            }
            else {
                if(s.equalsIgnoreCase(value)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
}
