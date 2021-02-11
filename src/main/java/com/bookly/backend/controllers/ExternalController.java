package com.bookly.backend.controllers;

import com.bookly.backend.models.carly.Car;
import com.bookly.backend.models.flatly.FlatsResultSet;
import com.bookly.backend.models.parkly.ParkingResultSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "External Systems Management")
@RestController
@AllArgsConstructor
@RequestMapping("/v1/external")
public class ExternalController {

    @Value(value = "carly.url")
    private String CARLY_API;
    @Value(value = "parkly.url")
    private String PARKLY_API;
    @Value(value = "flatly.url")
    private String FLATS_API;

    @GetMapping("/cars")
    @ApiOperation(value = "Fetches all the cars from Carly")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<Car[]> getCars(HttpServletRequest request) {
        String query = request.getQueryString();
        String url = query != null ? CARLY_API + "&" + query : CARLY_API;

        RestTemplate restTemplate = new RestTemplate();
        Car[] cars = restTemplate.getForObject(url, Car[].class);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/parkings")
    @ApiOperation(value = "Fetches all the parking places from Parkly")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<ParkingResultSet> getParkings(HttpServletRequest request) {
        String query = request.getQueryString();
        String url = query != null ? PARKLY_API + "&" + query : PARKLY_API;

        RestTemplate restTemplate = new RestTemplate();
        ParkingResultSet parkingResult = restTemplate.getForObject(url, ParkingResultSet.class);
        return new ResponseEntity<>(parkingResult, HttpStatus.OK);
    }

    @GetMapping("/flats")
    @ApiOperation(value = "Fetches all the parking places from Parkly")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<FlatsResultSet> getFlats(HttpServletRequest request) {
        String query = request.getQueryString();
        String url = query != null ? FLATS_API + "&" + query : FLATS_API;

        RestTemplate restTemplate = new RestTemplate();
        FlatsResultSet flatsResultSet = restTemplate.getForObject(url, FlatsResultSet.class);
        return new ResponseEntity<>(flatsResultSet, HttpStatus.OK);
    }
}
