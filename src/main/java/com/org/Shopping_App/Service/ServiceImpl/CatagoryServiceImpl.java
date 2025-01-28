package com.org.Shopping_App.Service.ServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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
		catagory.setStatus(catagoryDto.isStatus());
		catagoryRepo.save(catagory);

		return modelMapper.map(catagory, CatagoryDto.class);

	}

	@Override
	public CatagoryDto updateCatagory(int id, CatagoryDto catagoryDto, MultipartFile file) {

		// Fetch the category by ID or throw an exception if not found
		Catagory catagory = catagoryRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Catagory Not Found"));
		String imageName = "";
		if (!file.isEmpty()) {	
			imageName = file.getOriginalFilename();
		} else {
			imageName = catagory.getImageName();
		}

		// Update the category entity
		catagory.setName(catagoryDto.getName());
		catagory.setImageName(imageName);
		catagory.setStatus(catagoryDto.isStatus());

		// Save the updated category to the database
		Catagory save = catagoryRepo.save(catagory);
		if (!file.isEmpty()) {
			try {
				File saveFile = new ClassPathResource("static/img/category_img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// Return the updated DTO
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

	@Override
	public List<CatagoryDto> activCatagory(Integer pageNum, Integer pageSize) {
		List<Catagory> findByStatusTrue = catagoryRepo.findByStatusTrue();
		List<CatagoryDto> collect = findByStatusTrue.stream().map((e) -> modelMapper.map(e, CatagoryDto.class))
				.collect(Collectors.toList());
		return collect;
	}

}
