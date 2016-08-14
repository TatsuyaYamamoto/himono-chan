package net.sokontokoro_factory.himono.controller.resource;

import net.sokontokoro_factory.himono.controller.dto.Device;
import net.sokontokoro_factory.himono.controller.form.CreateDeviceForm;
import net.sokontokoro_factory.himono.controller.form.CreateUserForm;
import net.sokontokoro_factory.himono.exception.ConflictException;
import net.sokontokoro_factory.himono.exception.InvalidArgumentException;
import net.sokontokoro_factory.himono.exception.NoResourceException;
import net.sokontokoro_factory.himono.persistence.entity.DeviceEntity;
import net.sokontokoro_factory.himono.service.MasterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/devices")
public class DeviceRestController {
    @Autowired
    MasterService service;

    @Autowired
    ModelMapper modelMapper;

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
}
