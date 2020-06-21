package com.win.big.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.win.big.repository.RaceHistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/race_hist")
public class RaceHistController {

    @Autowired
    private RaceHistRepository raceHistRepository;

    @RequestMapping(value = "/getDividend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonNode getDividend(Model model, @RequestParam("date") String date) {
        return raceHistRepository.getRaceHistOne(date);
    }


    @RequestMapping(value = "/getDividend2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDividend2(Model model, @RequestParam("date") String date) {
        JsonNode node = raceHistRepository.getRaceHistOne(date);
        return new ResponseEntity<Object>(node, HttpStatus.OK);
    }
}
