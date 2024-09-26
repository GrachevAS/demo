package com.example.demo.controllers;

import com.example.demo.model.dto.request.CarToUserRequest;
import com.example.demo.model.dto.response.CarInfoResponse;
import com.example.demo.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.constants.Constants.CARS;

@Tag(name = "Автомобили")

@RestController
@RequestMapping(CARS)
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @Operation(summary = "Создать автомобиль")
    public CarInfoResponse createCar(@RequestBody @Valid CarInfoResponse request) {
        return carService.createCar(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить автомобиль по id")
    public CarInfoResponse getCar(@PathVariable Long id) {

        return carService.getCar(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить автомобиль по id")
    public CarInfoResponse updateCar(@PathVariable Long id, @RequestBody CarInfoResponse request) {
        return carService.updateCar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить автомобиль по id")
    public void deleteCar (@PathVariable Long id){
        carService.deleteCar(id);
    }
    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех"),
            @ApiResponse(responseCode = "404", description = "Не найден"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
    })
    @Operation(summary = "Получить список автомобилей")
    public Page<CarInfoResponse> getAllCars(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer perPage,
                                            @RequestParam(defaultValue = "brand") String sort,
                                            @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                            @RequestParam(required = false) String filter
    ) {
        return carService.getAllCars(page, perPage, sort, order, filter);
    }

    @PostMapping("/carToUser")
    @Operation(summary = "Добавить автомобиль пользователя")
    public void addCar(@RequestBody @Valid CarToUserRequest request) {
        carService.addCarToUser(request);
    }

//    @Operation(summary = "Получить список автомобилей по пользователю")
//    public Page<CarInfoResponse> getCarsByUser(@RequestParam(defaultValue = "1") Integer page,
//                                                @RequestParam(defaultValue = "10") Integer perPage,
//                                                @RequestParam(defaultValue = "brand") String sort,
//                                                @RequestParam(defaultValue = "ASC") Sort.Direction order,
//                                                @RequestParam(required = false) String filter
//    ) {
//        return carService.getCarsByUser(page, perPage, sort, order, filter);
//    }

}
