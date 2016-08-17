package net.sokontokoro_factory.himono.controller.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreateUserForm {
    @NotBlank(message = "UserID is required.")
    @JsonProperty("user_id")
    @Getter
    @Setter
    private String id;

    @NotBlank(message = "Username is required.")
    @JsonProperty("user_name")
    @Getter
    @Setter
    private String name;

    @NotBlank(message = "Addreess is required.")
    @JsonProperty("address")
    @Getter
    @Setter
    private String address;

    @NotBlank(message = "Password is required.")
    @JsonProperty("password")
    @Getter
    @Setter
    private String password;
}
