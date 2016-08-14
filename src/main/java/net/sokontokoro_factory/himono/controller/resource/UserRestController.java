package net.sokontokoro_factory.himono.controller.resource;

import net.sokontokoro_factory.himono.controller.dto.User;
import net.sokontokoro_factory.himono.controller.form.CreateDeviceForm;
import net.sokontokoro_factory.himono.controller.form.CreateUserForm;
import net.sokontokoro_factory.himono.exception.ConflictException;
import net.sokontokoro_factory.himono.exception.NoResourceException;
import net.sokontokoro_factory.himono.persistence.entity.UserEntity;
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
@RequestMapping("api/users")
public class UserRestController {
    @Autowired
    MasterService service;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(
            value = "{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getById(@PathVariable("userId") String userId){

        UserEntity userEntity;
        try {
            userEntity = service.getUserById(userId);
        } catch (NoResourceException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        User user = modelMapper.map(userEntity, User.class);

        return new ResponseEntity(user, HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity register(@RequestBody @Valid CreateUserForm form){

        try {
            service.registerUser(
                    form.getId(),
                    form.getName(),
                    form.getAddress(),
                    form.getPassword()
            );
        } catch (ConflictException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }
}