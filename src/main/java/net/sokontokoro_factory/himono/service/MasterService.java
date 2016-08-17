package net.sokontokoro_factory.himono.service;

import net.sokontokoro_factory.himono.exception.ConflictException;
import net.sokontokoro_factory.himono.exception.InvalidArgumentException;
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

    @Autowired
    DeviceRepository deviceRepo;

    @Autowired
    Authenticator authenticator;

    /**
     * 端末リストを取得する
     * @param offset
     * @param limit
     * @return
     */
    public List<DeviceEntity> getAllDevice(int offset, int limit){
        logger.traceEntry();
        return logger.traceExit(deviceRepo.findAll());
    }

    /**
     * 端末をID検索する
     *
     * @param deviceId
     * @return  該当なし
     */
    public DeviceEntity getDeviceById(String deviceId) throws NoResourceException {
        logger.traceEntry(deviceId);
        DeviceEntity device = deviceRepo.findOne(deviceId);

        if(device == null){
            logger.traceExit("requested device not found. ID: {}", deviceId);
            throw new NoResourceException();
        }

        return logger.traceExit(device);
    }

    /**
     * 端末をユーザーID検索する
     *
     * @param userId
     * @return
     */
    public List<DeviceEntity> getDeviceByUserId(String userId) throws NoResourceException {
        logger.traceEntry(userId);
        List<DeviceEntity> devices = deviceRepo.findByUserId(userId);

        return logger.traceExit(devices);
    }

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
        }

        return logger.traceExit(userRepo.findOne(userId));
    }

    /**
     * パスワード認証を行う
     *
     * @param userId
     * @return          認証OKの場合ユーザーEntity. 認証NGの場合null
     */
    public boolean validatePassword(String userId, String rawPassword){
        logger.traceEntry(userId, rawPassword);
        String encryptedPassword = authenticator.encryptPassword(rawPassword);

        UserEntity userEntity = userRepo.findByIdAndPassword(userId, encryptedPassword);

        if(userEntity == null){
            return logger.traceExit(false);
        }else{
            return logger.traceExit(true);
        }
    }

    /**
     * 端末を登録する
     *
     * @param id
     * @param name
     * @param userId
     * @throws ConflictException
     * @throws InvalidArgumentException
     */
    @Transactional(readOnly = false)
    public void registerDevice(
            String id,
            String name,
            String userId) throws ConflictException, InvalidArgumentException {
        logger.entry(id, name);

        // 端末IDの重複確認
        if(deviceRepo.exists(id)){
            logger.trace("cannot register a device as a result of to conflict (ID: {})", id);
            throw new ConflictException();
        }

        // 管理ユーザーの存在確認
        if(!userRepo.exists(userId)){
            logger.trace("requested user not found  (UserID: {})", userId);
            throw new InvalidArgumentException();
        }

        DeviceEntity newDevice = new DeviceEntity();
        newDevice.setId(id);
        newDevice.setName(name);
        newDevice.setUserId(userId);
        newDevice.setCreated(System.currentTimeMillis());

        deviceRepo.save(newDevice);
        logger.traceExit("success to save a new device, {}", newDevice);
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
