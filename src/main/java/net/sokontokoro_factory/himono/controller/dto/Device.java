package net.sokontokoro_factory.himono.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {

    @JsonProperty("id")
    @Getter
    @Setter
    private String id;

    @JsonProperty("name")
    @Getter
    @Setter
    private String name;

    @JsonProperty("manage_user_id")
    @Getter
    @Setter
    private String userId;

    @JsonProperty("created")
    @Getter
    @Setter
    private Long created;
}
