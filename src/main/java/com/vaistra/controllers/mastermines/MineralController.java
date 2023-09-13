package com.vaistra.controllers.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.MineralDto;
import com.vaistra.services.mastermines.MineralService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mineral")
public class MineralController {

    private final MineralService mineralService;

    @Autowired
    public MineralController(MineralService mineralService) {
        this.mineralService = mineralService;
    }

    @PostMapping
    private ResponseEntity<MineralDto> addMineral(@Valid @RequestBody MineralDto mineralDto)
    {
        return new ResponseEntity<>(mineralService.addMineral(mineralDto), HttpStatus.CREATED);
    }

    @GetMapping
    private ResponseEntity<HttpResponse> getAllMinerals(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                        @RequestParam(value = "sortBy", defaultValue = "mineralId", required = false) String sortBy,
                                                        @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(mineralService.getAllMinerals(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    
    @GetMapping("{mineralId}")
    public ResponseEntity<MineralDto> getMineralById(@PathVariable int mineralId)
    {
        return new ResponseEntity<>(mineralService.getMineralById(mineralId), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchMineralsByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "mineralId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(mineralService.searchMineralByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @PutMapping("{mineralId}")
    public ResponseEntity<MineralDto> updateMineral(@RequestBody MineralDto mineralDto, @PathVariable int mineralId)
    {
        return new ResponseEntity<>(mineralService.updateMineral(mineralDto, mineralId), HttpStatus.OK);
    }

    @DeleteMapping("{mineralId}")
    public ResponseEntity<String> deleteMineral(@PathVariable int mineralId)
    {
        return new ResponseEntity<>(mineralService.deleteMineral(mineralId), HttpStatus.OK);
    }
}
