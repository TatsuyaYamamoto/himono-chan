package net.sokontokoro_factory.himono.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @JsonProperty("id")
    @Getter
    @Setter
    private String id;

    @JsonProperty("name")
    @Getter
    @Setter
    private String name;

    @JsonProperty("address")
    @Getter
    @Setter
    private String address;

    @JsonProperty("created")
    @Getter
    @Setter
    private Long created;

    @JsonProperty("devices")
    @Getter
    @Setter
    private List<Device> devices;
}
