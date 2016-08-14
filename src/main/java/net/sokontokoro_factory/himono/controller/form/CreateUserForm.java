package net.sokontokoro_factory.himono.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

public class CreateUserForm {
    @NotBlank(message = "UserID is required.")
    @Getter
    @Setter
    private String id;

    @NotBlank(message = "Username is required.")
    @Getter
    @Setter
    private String name;

    @NotBlank(message = "Addreess is required.")
    @Getter
    @Setter
    private String address;

    @NotBlank(message = "Password is required.")
    @Getter
    @Setter
    private String password;
}
