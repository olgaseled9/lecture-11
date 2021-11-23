package by.itacademy.javaenterprise.seledtsova.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private Long customerId;
    private String firstName;
    private String lastName;

}
