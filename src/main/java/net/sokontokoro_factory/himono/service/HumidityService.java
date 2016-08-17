package net.sokontokoro_factory.himono.service;

import net.sokontokoro_factory.himono.exception.InvalidArgumentException;
import net.sokontokoro_factory.himono.exception.NoResourceException;
import net.sokontokoro_factory.himono.persistence.entity.HumidityEntity;
import net.sokontokoro_factory.himono.persistence.repository.DeviceRepository;
import net.sokontokoro_factory.himono.persistence.repository.HumidityRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class HumidityService {
    private static Logger logger = LogManager.getLogger(HumidityService.class);

    @Autowired
    HumidityRepository humidityRepository;

    @Autowired
    DeviceRepository deviceRepository;

    /**
     * 端末の乾湿データのリストを取得する
     *
     * @return
     */
    public List<HumidityEntity> getList(String deviceId, long offset, long limit) throws NoResourceException {
        logger.entry(deviceId, offset, limit);

        if(!deviceRepository.exists(deviceId)){
            logger.trace("requested device is not exist. (device id: {})", deviceId);
            throw new NoResourceException();
        }

        List<HumidityEntity> humidityList = humidityRepository
                .findByDeviceId(deviceId)
                .stream()
                .filter(new Predicate<HumidityEntity>() {
                    @Override
                    public boolean test(HumidityEntity humidityEntity) {
                        long created = humidityEntity.getCreated();

                        return (offset < created && created < limit);
                    }
                }).collect(Collectors.toList());

        logger.traceExit("find humidity list. count: {}", humidityList.size());
        return humidityList;
    }

    /**
     * 乾湿値を登録する
     *
     * @param value
     * @param deviceId
     * @throws InvalidArgumentException
     */
    @Transactional(readOnly = false)
    public void register(int value, String deviceId) throws InvalidArgumentException {
        logger.entry(value, deviceId);

        if(!deviceRepository.exists(deviceId)){
            logger.trace("cannot register as a result of not to exist device. (device id: {})", deviceId);
            throw new InvalidArgumentException();
        }
        HumidityEntity humidityEntity = new HumidityEntity();
        humidityEntity.setValue(value);
        humidityEntity.setDeviceId(deviceId);
        humidityEntity.setCreated(System.currentTimeMillis());

        humidityRepository.save(humidityEntity);
        logger.traceExit("success to save a new humidity, {}", humidityEntity);
    }
}