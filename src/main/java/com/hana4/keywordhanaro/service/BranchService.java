package com.hana4.keywordhanaro.service;

import java.util.List;

import com.hana4.keywordhanaro.model.dto.BranchDto;

public interface BranchService {

	List<BranchDto> searchBranch(String query, Double lat, Double lng);

}
