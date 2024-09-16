package com.example.demo.service;

import com.example.demo.model.dto.response.CarInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class CarService {


    public CarInfoResponse createCar(@Valid CarInfoResponse request) {
        return null;
    }

    public CarInfoResponse getCar(Long id) {
        return null;

    }

    public CarInfoResponse updateCar(Long id, CarInfoResponse request) {
        return null;

    }

    public void deleteCar(Long id) {

    }

    public List<CarInfoResponse> getAllCars() {
        return null;
    }
}

