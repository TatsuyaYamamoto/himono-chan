package net.sokontokoro_factory.himono.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "DEVICE")
@ToString
public class DeviceEntity {
    /**
     * 端末識別子。MACアドレスとか
     */
    @Id
    @Getter
    @Setter
    private String id;

    /**
     * 端末名
     */
    @Column(name = "NAME", nullable = false, unique = true)
    @Getter
    @Setter
    private String name;

    /**
     * 管理ユーザーID
     */
    @Column(name = "USER_ID", nullable = false)
    @Getter
    @Setter
    private String userId;

    /**
     * 登録日
     */
    @Column(name = "CREATED", nullable = false)
    @Getter
    @Setter
    private Long created;

    /**
     * リレーション userオブジェクト
     */
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @Getter
    @Setter
    private UserEntity userEntity;
}
