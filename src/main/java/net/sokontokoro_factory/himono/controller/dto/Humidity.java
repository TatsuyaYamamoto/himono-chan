package net.sokontokoro_factory.himono.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Humidity {
    /**
     * 乾湿値
     */
    @JsonProperty("value")
    @Getter
    @Setter
    private Integer value;

    /**
     * 測定日
     */
    @JsonProperty("measured")
    @Getter
    @Setter
    private Long measured;
}
