package net.sokontokoro_factory.himono.persistence.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
@ToString
public class UserEntity {

    /**
     * ID
     */
    @Id
    @Getter
    @Setter
    private String id;


    /**
     * アプリとかの表示名
     */
    @Column(name = "NAME", nullable = false, unique = false)
    @Getter
    @Setter
    private String name;

    /**
     * パスワード
     */
    @Column(name = "PASSWORD", nullable = false)
    @Getter
    @Setter
    private String password;

    /**
     * 通知アドレス
     */
    @Column(name = "ADDRESS", nullable = false)
    @Getter
    @Setter
    private String address;

    /**
     * 登録日
     */
    @Column(name = "CREATED", nullable = false)
    @Getter
    @Setter
    private Long created;
}
