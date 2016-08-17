package net.sokontokoro_factory.himono.persistence.repository;

import net.sokontokoro_factory.himono.persistence.entity.DeviceEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface DeviceRepository extends JpaRepository<DeviceEntity, String>, JpaSpecificationExecutor<DeviceEntity> {
    List<DeviceEntity> findByUserId(String userId);
}