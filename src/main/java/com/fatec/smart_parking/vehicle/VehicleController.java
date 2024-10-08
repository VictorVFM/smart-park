package com.fatec.smart_parking.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/plate/{plate}")
    public ResponseEntity<VehicleDTO> findByPlate(@PathVariable String plate){
        VehicleDTO vehicle = vehicleService.findByPlate(plate);
        return ResponseEntity.ok().body(vehicle);
    }


}
