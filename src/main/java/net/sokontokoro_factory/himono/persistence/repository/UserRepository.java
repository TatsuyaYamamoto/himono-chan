package net.sokontokoro_factory.himono.persistence.repository;


import net.sokontokoro_factory.himono.persistence.entity.UserEntity;
import net.sokontokoro_factory.himono.persistence.entity.UserEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findByIdAndPassword(String userId, String password);
}