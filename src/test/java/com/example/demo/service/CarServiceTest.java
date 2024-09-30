package com.example.demo.service;

import com.example.demo.model.db.entity.Car;
import com.example.demo.model.db.entity.User;
import com.example.demo.model.db.repository.CarRepository;
import com.example.demo.model.dto.request.CarInfoRequest;
import com.example.demo.model.dto.request.CarToUserRequest;
import com.example.demo.model.dto.request.UserInfoRequest;
import com.example.demo.model.dto.response.CarInfoResponse;
import com.example.demo.model.dto.response.UserInfoResponse;
import com.example.demo.model.enums.CarStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private UserService userService;

    @Mock
    private CarRepository carRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createCar() {
        CarInfoRequest request = new CarInfoRequest();
        Car car = new Car();
        car.setId(1L);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        CarInfoResponse result = carService.createCar(request);
        assertEquals(car.getId(), result.getId());
    }

    @Test
    public void getCar() {
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
        CarInfoResponse result = carService.getCar(1L);
        assertEquals(car.getId(), result.getId());
    }

    @Test
    public void updateCar() {
        CarInfoRequest request = new CarInfoRequest();
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        carService.updateCar(car.getId(), request);
        verify(carRepository, times(1)).save(any(Car.class));
        assertEquals(CarStatus.UPDATED, car.getStatus());
    }

    @Test
    public void deleteCar() {
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        carService.deleteCar(car.getId());
        verify(carRepository, times(1)).save(any(Car.class));
        assertEquals(CarStatus.DELETED, car.getStatus());
    }

    @Test
    public void getAllCars() {
        Car car1 = new Car();
        car1.setId(1L);
        car1.setBrand("BMW");
        Car car2 = new Car();
        car2.setId(2L);
        List<Car> cars = List.of(car1, car2);
        Page<Car> pagedCars = new PageImpl<>(cars);
        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, car1.getBrand());
        when(carRepository.findAllByStatusNot(pageRequest, CarStatus.DELETED)).thenReturn(pagedCars);
        Page <CarInfoResponse> result = carService
                .getAllCars(0, 10, car1.getBrand(), Sort.Direction.ASC, null);
        assertEquals(pagedCars.getTotalElements(), result.getTotalElements());
    }
    @Test
    public void addCarToUser() {
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        User user = new User();
        user.setId(1L);
        user.setCars(new ArrayList<>());
        when(userService.getUserFromDB(user.getId())).thenReturn(user);
        when(userService.updateUserData(any(User.class))).thenReturn(user);
        CarToUserRequest request = CarToUserRequest.builder()
                .carId(car.getId())
                .userId(user.getId())
                .build();
        carService.addCarToUser(request);
        verify(carRepository, times(1)).save(any(Car.class));
        assertEquals(user.getId(), car.getUser().getId());
    }


    @Test
    public void getCarsByUser() {
        User user = new User();
        user.setId(1L);
        when(userService.getUserFromDB(user.getId())).thenReturn(user);
        List<Car> userCars = List.of(new Car(), new Car());
        user.setCars(userCars);
        List<CarInfoResponse> result = carService.getCarsByUser(user.getId());
        assertEquals(userCars.size(), result.size());
    }
}