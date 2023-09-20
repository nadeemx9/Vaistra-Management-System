package com.vaistra.services.mastermines;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.mastermines.MineralDto;
import com.vaistra.dto.mastermines.MineralUpdateDto;

public interface MineralService {

    MineralDto addMineral(MineralDto mineralDto);
    MineralDto getMineralById(int mineralId);
    HttpResponse getAllMinerals(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse searchMineralByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection);
    MineralDto updateMineral(MineralUpdateDto mineralDto, int mineralId);
    String deleteMineral(int mineralId);
}
