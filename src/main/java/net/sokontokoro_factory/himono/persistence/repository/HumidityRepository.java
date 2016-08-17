package net.sokontokoro_factory.himono.persistence.repository;

import net.sokontokoro_factory.himono.persistence.entity.HumidityEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface HumidityRepository extends JpaRepository<HumidityEntity, Integer>, JpaSpecificationExecutor<HumidityEntity> {
    List<HumidityEntity> findByDeviceId(String deviceId);
}