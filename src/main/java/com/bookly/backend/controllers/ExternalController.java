package com.bookly.backend.controllers;

import com.bookly.backend.models.Car;
import com.bookly.backend.models.FlatsResultSet;
import com.bookly.backend.models.ParkingResultSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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

    private final String CARLY_API = "http://52.3.250.46:5050/public/cars?apiKey=BTKJPMKRP";
    private final String PARKLY_API = "http://parkly-env.eba-u2qumtf7.us-east-2.elasticbeanstalk.com/b/parkings?apiKey=1AC4FCOPR";
    private final String FLATS_API = "http://flatly-env.eba-pftr9jj2.eu-central-1.elasticbeanstalk.com/ext/flats?apiKey=savekey";

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
