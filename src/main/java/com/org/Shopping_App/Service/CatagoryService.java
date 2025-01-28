package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.CatagoryDto;

public interface CatagoryService {

	public CatagoryDto saveCatagory(CatagoryDto catagoryDto);

	public CatagoryDto updateCatagory(int id, CatagoryDto catagoryDto , MultipartFile file);
	
	public void deleteCatagory(int id);
	
	public List<CatagoryDto> fetchAllCatagory();
	
	public boolean isCatagoryExist(String name);
	
	public CatagoryDto findById(int id);
	
	public List<CatagoryDto> activCatagory(Integer pageNum, Integer pageSize);

}
