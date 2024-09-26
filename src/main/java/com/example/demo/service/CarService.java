package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.Car;
import com.example.demo.model.db.entity.User;
import com.example.demo.model.db.repository.CarRepository;
import com.example.demo.model.dto.request.CarToUserRequest;
import com.example.demo.model.dto.response.CarInfoResponse;
import com.example.demo.model.dto.response.UserInfoResponse;
import com.example.demo.model.enums.CarStatus;
import com.example.demo.model.enums.UserStatus;
import com.example.demo.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class CarService {

    private final UserService userService;
    private final CarRepository carRepository;
    private final ObjectMapper mapper;

    public CarInfoResponse createCar(CarInfoResponse request) {
        Car car = mapper.convertValue(request, Car.class);
        car.setStatus(CarStatus.CREATED);
        return mapper.convertValue(carRepository.save(car), CarInfoResponse.class);
    }

    public CarInfoResponse getCar(Long id) {
        return mapper.convertValue(getCarById(id), CarInfoResponse.class);

    }

    private Car getCarById(Long id) {
        return carRepository.findById(id).orElseThrow(()-> new CustomException("Car not found", HttpStatus.NOT_FOUND));//попробовать добавить id
    }

    public CarInfoResponse updateCar(Long id, CarInfoResponse request) {

        Car car = getCarById(id);

        car.setBrand(request.getBrand() == null ? car.getBrand() : request.getBrand());
        car.setModel(request.getModel() == null ? car.getModel() : request.getModel());
        car.setColor(request.getBrand() == null ? car.getColor() : request.getColor());
        car.setYear(request.getYear() == null ? car.getYear() : request.getYear());
        car.setPrice(request.getPrice() == null ? car.getPrice() : request.getPrice());
        car.setIsNew(request.getIsNew() == null ? car.getIsNew() : request.getIsNew());

        car.setUpdatedAt(LocalDateTime.now());
        car.setStatus(CarStatus.UPDATED);

        Car save = carRepository.save(car);

        return mapper.convertValue(save, CarInfoResponse.class);
    }

    public void deleteCar(Long id) {
        Car car = getCarById(id);
        car.setStatus(CarStatus.DELETED);
        carRepository.save(car);
    }

    public Page<CarInfoResponse> getAllCars(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {

        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Car> all;
        if (filter == null) {
            all = carRepository.findAllByStatusNot(pageRequest, CarStatus.DELETED);
        } else {
            all = carRepository.findAllByStatusNotFiltered(pageRequest, CarStatus.DELETED, filter.toLowerCase());
        }
        List<CarInfoResponse> content = all.getContent().stream()
                .map(car -> mapper.convertValue(car, CarInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());

    }

    public void addCarToUser(@Valid CarToUserRequest request) {
        Car car = carRepository.findById(request.getCarId()).orElseThrow(()-> new CustomException("Car not found", HttpStatus.NOT_FOUND));//поробовать с id

        User userFromDB = userService.getUserFromDB(request.getUserId());

        userFromDB.getCars().add(car);

        userService.updateUserData(userFromDB);

        car.setUser(userFromDB);
        carRepository.save(car);
    }

    public Car getSomeCar(){
        return carRepository.getSomeCar(true);
    }

//    public Page<CarInfoResponse> getUserCars(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
//
//        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
//
//        Page<Car> userCars;
//        if (filter == null) {
//            userCars = carRepository.findA(pageRequest, CarStatus.DELETED);
//        } else {
//            userCars = carRepository.findAllByStatusNotFiltered(pageRequest, CarStatus.DELETED, filter.toLowerCase());
//        }
//        List<CarInfoResponse> content = userCars.getContent().stream()
//                .map(car -> mapper.convertValue(car, CarInfoResponse.class))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(content, pageRequest, userCars.getTotalElements());

//    }

//    public Page<CarInfoResponse> getCarsByUser(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
//
//        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
//
//        Page<Car> userCars;
//        if (filter == null) {
//            userCars = carRepository;
//        } else {
//            userCars = carRepository.findAllByStatusNotFiltered(pageRequest, CarStatus.DELETED, filter.toLowerCase());
//        }
//        List<CarInfoResponse> content = userCars.getContent().stream()
//                .map(car -> mapper.convertValue(car, CarInfoResponse.class))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(content, pageRequest, userCars.getTotalElements());
//    }
}

