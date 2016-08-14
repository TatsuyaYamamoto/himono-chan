package net.sokontokoro_factory.himono.service;

import net.sokontokoro_factory.himono.exception.ConflictException;
import net.sokontokoro_factory.himono.exception.NoResourceException;
import net.sokontokoro_factory.himono.persistence.entity.DeviceEntity;
import net.sokontokoro_factory.himono.persistence.entity.UserEntity;
import net.sokontokoro_factory.himono.persistence.entity.UserEntity;
import net.sokontokoro_factory.himono.persistence.repository.DeviceRepository;
import net.sokontokoro_factory.himono.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * ユーザー、端末リソースの操作を行うサービスクラス
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class MasterService {
    private static Logger logger = LogManager.getLogger(MasterService.class);

    @Autowired
    UserRepository userRepo;

    /**
     * ユーザーリストを取得する
     * @param offset
     * @param limit
     * @return
     */
    public List<UserEntity> getAllUser(int offset, int limit){
        logger.traceEntry();
        return logger.traceExit(userRepo.findAll());
    }

    /**
     * ユーザーをID検索する
     *
     * @param userId
     * @return  該当なし
     */
    public UserEntity getUserById(String userId) throws NoResourceException {
        logger.traceEntry(userId);

        UserEntity user = userRepo.getOne(userId);

        if(user == null){
            logger.traceExit("requested user not found. ID: {}", userId);
            throw new NoResourceException();
        }else {
            return logger.traceExit(userRepo.findOne(userId));
        }
    }

    /**
     * ユーザーを登録する
     *
     * @param id
     * @param name
     * @param address
     * @param rawPassword
     * @throws ConflictException
     */
    @Transactional(readOnly = false)
    public void registerUser(
            String id,
            String name,
            String address,
            String rawPassword) throws ConflictException {
        logger.entry(id, name);

        if(userRepo.exists(id)){
            logger.trace("cannot register a device as a result of to conflict (ID: {})", id);
            throw new ConflictException();
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(name);
        userEntity.setAddress(address);
        userEntity.setPassword(authenticator.encryptPassword(rawPassword));
        userEntity.setCreated(System.currentTimeMillis());

        userRepo.save(userEntity);
        logger.traceExit("success to save a new user, {}", userEntity);
    }
}
