package com.lendbiz.gara.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lendbiz.gara.constants.Constants;
import com.lendbiz.gara.constants.ErrorCode;
import com.lendbiz.gara.dto.response.FileResponse;
import com.lendbiz.gara.entity.GrFileInfo;
import com.lendbiz.gara.model.exception.BusinessException;
import com.lendbiz.gara.repository.GrFileInfoRepository;
import com.lendbiz.gara.service.BaseService;
import com.lendbiz.gara.service.FilesStorageService;

@Service("filesStorageService")
public class FilesStorageServiceImpl extends BaseService implements FilesStorageService {
	public FilesStorageServiceImpl(Environment env) {
		super(env);
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	GrFileInfoRepository fileRepository;

	private final String root = Constants.DIRECTORIES_ROOT;
	private Path direct;

	@Override
	public String init(String key, String custId) {
		String url = root + custId + "\\" + key;
		direct = Paths.get(url);

		if (!Files.exists(direct)) {
			try {
				Files.createDirectories(direct);
				logger.info("Create path = " + direct + " successfully!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logger.info("Path = " + direct + " is existed!");
		}

		return direct.toString();
	}

	@Override
	public void save(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.direct.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.FAILED_SAVE_FILE,
					ErrorCode.FAILED_SAVE_FILE_DESCRIPTION + e.getMessage());
		}
	}

	@Override
	public void saveContract(byte[] file, String path) {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(file);
			// fos.close(); There is no more need for this line since you had created the
			// instance of "fos" inside the try. And this will automatically close the
			// OutputStream
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.FAILED_SAVE_FILE,
					ErrorCode.FAILED_SAVE_FILE_DESCRIPTION + e.getMessage());
		}
	}

	@Override
	public void saveContract(byte[] file, String path, String fileName, String key, String custId) {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			logger.info("[saveContract] start write data to file: {}", path);
			fos.write(file);
			fos.close();

			logger.info("[saveContract] start save contracted to db");
			FileResponse fileInfo = new FileResponse();
			fileInfo.setName(fileName);
			fileInfo.setUrl(path);

			// save info to db
			saveFileToDB(fileInfo, key, custId, "Ä�Ã£ kÃ½ há»£p Ä‘á»“ng", Constants.KEY_CONTRACTED);

			logger.info("[saveContract] Uploaded the file successfully: ", fileName);
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.FAILED_SAVE_FILE,
					ErrorCode.FAILED_SAVE_FILE_DESCRIPTION + e.getMessage());
		}
	}

	private void saveFileToDB(FileResponse fileInfo, String key, String custId, String description, String type) {
		GrFileInfo entity = new GrFileInfo();
		entity.setId(UUID.randomUUID().toString());
		entity.setCustId(custId);
		entity.setKey(key);
		entity.setValue(fileInfo.getUrl());
		entity.setType(type);
		entity.setDescription(description);
		entity.setCreateDate(new Date(Calendar.getInstance().getTimeInMillis()));
		entity.setName(fileInfo.getName());

		try {
			fileRepository.save(entity);
		} catch (Exception e) {
			logger.info("[API UPLOAD] Can not save file to DB: {}", fileInfo.getName());
		}
	}

}
