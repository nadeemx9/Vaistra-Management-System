package com.vaistra.controllers.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.EntityDto;
import com.vaistra.services.mastermines.EntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("entity")
public class EntityController {

    private final EntityService entityService;

    @Autowired
    public EntityController(EntityService entityService) {
        this.entityService = entityService;
    }

    @PostMapping
    public ResponseEntity<EntityDto> addEntity(@Valid @RequestBody EntityDto entityDto)
    {
        return new ResponseEntity<>(entityService.addEntity(entityDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllEntities(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                       @RequestParam(value = "sortBy", defaultValue = "entityId", required = false) String sortBy,
                                                       @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(entityService.getAllEntities(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("{entityId}")
    public ResponseEntity<EntityDto> getEntityById(@PathVariable int entityId)
    {
        return new ResponseEntity<>(entityService.getEntityById(entityId), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchEntitiesByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "entityId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(entityService.searchEntityByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{entityId}")
    public ResponseEntity<EntityDto> updateEntity(@RequestBody EntityDto entityDto, @PathVariable int entityId)
    {
        return new ResponseEntity<>(entityService.updateEntity(entityDto, entityId), HttpStatus.OK);
    }

    @DeleteMapping("{entityId}")
    public ResponseEntity<String> deleteEntity(@PathVariable int entityId)
    {
        return new ResponseEntity<>(entityService.deleteEntity(entityId), HttpStatus.OK);
    }
}
