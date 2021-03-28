/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.projectBackend.exceptions;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Adam Bublavý
 */
@XmlRootElement
@Getter
public class ApiException {
    
    @XmlElement
    private HttpStatus httpStatus;
    @XmlElement
    private LocalDateTime localDateTime;
    @XmlElement
    private String message;
    
    public ApiException(HttpStatus httpStatus, Throwable ex) {
        this.httpStatus = httpStatus;
        this.message = ex.getMessage();
        this.localDateTime = LocalDateTime.now();
    }
    
}
