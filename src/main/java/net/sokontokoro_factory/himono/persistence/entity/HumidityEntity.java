package net.sokontokoro_factory.himono.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "HUMIDITY")
@ToString
public class HumidityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    /**
     * 乾湿値
     */
    @Column(name = "VALUE", nullable = false)
    @Getter
    @Setter
    private Integer value;

    /**
     * 端末ID
     */
    @Column(name = "DEVICE_ID", nullable = false)
    @Getter
    @Setter
    private String deviceId;

    /**
     * 測定日
     */
    @Column(name = "CREATED", nullable = false)
    @Getter
    @Setter
    private Long created;

    /**
     * リレーション deviceオブジェクト
     */
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private DeviceEntity deviceEntity;
}
