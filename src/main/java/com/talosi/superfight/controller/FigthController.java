package com.talosi.superfight.controller;

import com.talosi.superfight.dto.FigthDto;
import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.service.FightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/figths")
@AllArgsConstructor
public class FigthController {

    private FightService figthService;

    @PostMapping
    public FigthDto fight(@RequestBody FigthDto figth) throws Exception{
        return figthService.fight(figth);
    }

}
