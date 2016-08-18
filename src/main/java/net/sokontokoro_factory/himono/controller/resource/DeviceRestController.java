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
     * @api {get} /api/devices/{device_id} get device data
     * @apiGroup Devices
     * @apiVersion 1.0.0
     * @apiDescription read device
     * @apiPermission admin
     *
     * @apiParam {String}     deviceId          Device ID
     *
     *
     * @apiSuccess {Integer}    id              Device ID
     * @apiSuccess {Long}       name            Device name
     * @apiSuccess {Integer}    manage_user_id  user id who manages a taget device.
     * @apiSuccess {String}     created         date registerd
     *
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *         "id": "12:23:34:56:77",
     *         "name": "devicename",
     *         "manage_user_id": "aiueo",
     *         "created": 1234567890
     *     }
     *
     * @apiError Device has not found.
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 400 Bad Request
     *
     * @apiError System doesn't have any data.
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 404 Not Found
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
     * @api {get} /api/devices register device
     * @apiGroup Devices
     * @apiVersion 1.0.0
     * @apiDescription post device
     * @apiPermission admin
     *
     * @apiParam {String}       deviceId        Device ID
     *
     *
     * @apiSuccess {Integer}    device_id       Device ID
     * @apiSuccess {Long}       device_name     Device name
     * @apiSuccess {Integer}    manage_user_id  user id who manages a taget device.
     *
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 201 Created
     *
     * @apiError DeviceId already exists.
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 409 Conflict
     *
     * @apiError User donesn't exist.
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 400 Bad Request
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
     * @api {get} /api/devices/{device_id}/humidities get humidity data
     * @apiGroup Collections
     * @apiVersion 1.0.0
     * @apiDescription read list of humidity
     * @apiPermission admin
     *
     * @apiParam {String}     deviceId          Device ID
     * @apiParam {String}     offset_time       start date of data that system provides
     * @apiParam {String}     limit_time        end date of data that system provides
     *
     *
     * @apiSuccess {Array}      humidities      Array of Humidity data.
     * @apiSuccess {Integer}    value           value of humidiry
     * @apiSuccess {Long}       measured        measured date
     * @apiSuccess {Integer}    count           number of humidities.
     * @apiSuccess {String}     device_id       measured device ID
     * @apiSuccess {String}     device_name     measured device name
     * @apiSuccess {Long}       offset_time     start date of data that system provides
     * @apiSuccess {Long}       limit_time      end date of data that system provides
     *
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *         "humidities": [
     *             {
     *                 "value": "123-456-789",
     *                 "measured": 1234567890,
     *             },
     *             ...
     *         ],
     *         "count": 25252,
     *         "device_id": 12:23:34:56:77,
     *         "device_name": "DeviceName",
     *         "offset_time": 1234567890,
     *         "limit_time": 4567890123
     *     }
     *
     * @apiError Device has not found.
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 400 Bad Request
     *
     * @apiError System doesn't have any data.
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 404 Not Found
     */
    @RequestMapping(
            value = "/{device_id}/humidities",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity get(
            @PathVariable("device_id")
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
     * @api {get} /api/devices/{device_id}/humidities register humidity data
     * @apiGroup Humidity
     * @apiVersion 1.0.0
     * @apiDescription register humidity data
     * @apiPermission
     *
     * @apiParam {String}     device_id DeviceID
     * @apiParam {String}     value     Measured value of humidity
     *
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 200 OK
     *
     * @apiError Device has not found.
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 400 Bad Request
     */
    @RequestMapping(
            value = "/{device_id}/humidities",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity post(
            @RequestBody
                    PostHumidityForm form,
            @PathVariable("device_id")
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