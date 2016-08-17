package net.sokontokoro_factory.himono.controller.resource;

import net.sokontokoro_factory.himono.controller.dto.Device;
import net.sokontokoro_factory.himono.controller.dto.Humidity;
import net.sokontokoro_factory.himono.controller.form.CreateDeviceForm;
import net.sokontokoro_factory.himono.controller.form.PostHumidityForm;
import net.sokontokoro_factory.himono.exception.ConflictException;
import net.sokontokoro_factory.himono.exception.InvalidArgumentException;
import net.sokontokoro_factory.himono.exception.NoResourceException;
import net.sokontokoro_factory.himono.persistence.entity.DeviceEntity;
import net.sokontokoro_factory.himono.persistence.entity.HumidityEntity;
import net.sokontokoro_factory.himono.service.HumidityService;
import net.sokontokoro_factory.himono.service.MasterService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/devices")
public class DeviceRestController {
    @Autowired
    MasterService service;

    @Autowired
    private HumidityService humidityService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * 端末情報を取得する
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(
            value = "{deviceId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getById(@PathVariable String deviceId){

        DeviceEntity deviceEntity;
        try {
            deviceEntity = service.getDeviceById(deviceId);
        } catch (NoResourceException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Device device = modelMapper.map(deviceEntity, Device.class);
        return new ResponseEntity(device, HttpStatus.OK);
    }

    /**
     * 端末を登録する
     *
     * @param form
     * @return
     */
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity register(@RequestBody @Valid CreateDeviceForm form){

        try {
            service.registerDevice(
                    form.getId(),
                    form.getName(),
                    form.getUserId()
            );
        } catch (ConflictException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (InvalidArgumentException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 乾湿データを取得する
     *
     * @param deviceId
     * @param offsetTime
     * @param limitTime
     * @return
     */
    @RequestMapping(
            value = "/{deviceId}/humidities",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity get(
            @PathVariable("deviceId")
                    String deviceId,
            @RequestParam(name = "offset_time", required = false)
                    Long offsetTime,
            @RequestParam(name = "limit_time", required = false)
                    Long limitTime){

        /* 初期値確認 */
        Long now = System.currentTimeMillis();
        if(limitTime == null){
            limitTime = now;
        }
        if(offsetTime == null){
            // 30日間
            long defaultPeriod = 30 * 24 * 60 * 60 * 1000L;
            offsetTime = now - defaultPeriod;
        }

        /* 検索実行 */
        List<HumidityEntity> humidityEntities = null;
        try {
            humidityEntities = humidityService.getList(deviceId, offsetTime, limitTime);
        } catch (NoResourceException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        // 登録件数0の場合
        if(humidityEntities.size() == 0){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        // レスポンスオブジェクト作成
        List<Humidity> humidities = new ArrayList();
        for(HumidityEntity entity: humidityEntities){
            Humidity humidity = new Humidity();
            humidity.setValue(entity.getValue());
            humidity.setMeasured(entity.getCreated());

            humidities.add(humidity);
        }
        Map response = new HashMap();
        response.put("humidities", humidities);
        response.put("count", humidities.size());
        response.put("device_id", humidityEntities.get(0).getDeviceId());
        response.put("device_name", humidityEntities.get(0).getDeviceEntity().getName());
        response.put("offset_time", offsetTime);
        response.put("limit_time", limitTime);
        return new ResponseEntity(response, HttpStatus.OK);
    }


    /**
     * 乾湿値を登録する
     *
     * @param form
     * @param deviceId
     * @param uriBuilder
     * @return
     */
    @RequestMapping(
            value = "/{deviceId}/humidities",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity post(
            @RequestBody
                    PostHumidityForm form,
            @PathVariable("deviceId")
                    String deviceId,
            UriComponentsBuilder uriBuilder){

        try{
            humidityService.register(form.getValue(), deviceId);
        }catch (InvalidArgumentException e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }
}