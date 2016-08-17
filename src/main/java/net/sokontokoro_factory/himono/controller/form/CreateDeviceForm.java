package net.sokontokoro_factory.himono.controller.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class CreateDeviceForm {
    @NotBlank
    @JsonProperty("device_id")
    @Getter
    @Setter
    private String id;

    @NotBlank
    @JsonProperty("device_name")
    @Getter
    @Setter
    private String name;

    @NotBlank
    @JsonProperty("manage_user_id")
    @Getter
    @Setter
    private String userId;
}
