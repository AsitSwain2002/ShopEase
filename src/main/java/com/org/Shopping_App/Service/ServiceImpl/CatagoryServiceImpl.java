package com.org.Shopping_App.Service.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.CatagoryDto;
import com.org.Shopping_App.Entity.Catagory;
import com.org.Shopping_App.Repo.CatagoryRepo;
import com.org.Shopping_App.Service.CatagoryService;
import com.org.Shopping_App.exceptionHandler.ResourceNotFound;

@Service
public class CatagoryServiceImpl implements CatagoryService {

	@Autowired
	private CatagoryRepo catagoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CatagoryDto saveCatagory(CatagoryDto catagoryDto) {

		Catagory catagory = modelMapper.map(catagoryDto, Catagory.class);
		catagory.setName(catagoryDto.getName());
		catagory.setImageName(catagoryDto.getImageName());
		catagory.setStatus(catagoryDto.getStatus());
		catagoryRepo.save(catagory);

		return modelMapper.map(catagory, CatagoryDto.class);

	}

	@Override
	public CatagoryDto updateCatagory(int id, CatagoryDto catagoryDto, MultipartFile file) {

		Catagory catagory = catagoryRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Catagory Not Found"));
		String imageName = "";
		if (!file.isEmpty()) {
			imageName = file.getOriginalFilename();
		} else {
			imageName = catagory.getImageName();
		}
		catagory.setName(catagoryDto.getName());
		catagory.setImageName(imageName);
		catagory.setStatus(catagoryDto.getStatus());
		catagoryRepo.save(catagory);
		return modelMapper.map(catagory, CatagoryDto.class);
	}

	@Override
	public void deleteCatagory(int id) {

		Catagory catagory = catagoryRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Catagory Not Found"));
		catagoryRepo.deleteById(id);
	}

	@Override
	public List<CatagoryDto> fetchAllCatagory() {

		List<Catagory> catagories = catagoryRepo.findAll();
		return catagories.stream().map((e) -> modelMapper.map(e, CatagoryDto.class)).collect(Collectors.toList());
	}

	@Override
	public boolean isCatagoryExist(String name) {
		return catagoryRepo.existsByName(name);
	}

	@Override
	public CatagoryDto findById(int id) {
		Catagory catagory = catagoryRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Catagory Not Found"));
		return modelMapper.map(catagory, CatagoryDto.class);
	}

}
