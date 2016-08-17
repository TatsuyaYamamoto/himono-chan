package net.sokontokoro_factory.himono.controller.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PostHumidityForm {
    @NotBlank(message = "UserID is required.")
    @JsonProperty("value")
    @Getter
    @Setter
    Integer value;
}