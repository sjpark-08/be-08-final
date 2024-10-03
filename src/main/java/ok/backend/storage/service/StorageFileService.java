package ok.backend.storage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ok.backend.common.exception.CustomException;
import ok.backend.common.exception.ErrorCode;
import ok.backend.storage.domain.entity.StorageFile;
import ok.backend.storage.domain.repository.StorageFileRepository;
import ok.backend.storage.dto.StorageResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StorageFileService {

    private final StorageFileRepository storageFileRepository;

    @Value("${spring.servlet.multipart.location}")
    private String basePath;

    public StorageResponseDto save(StorageFile storageFile){
        return new StorageResponseDto(storageFileRepository.save(storageFile));
    }

    public List<StorageResponseDto> findAllStorageFilesByStorageId(Long storageId) {
        return storageFileRepository.findAllStorageFilesByStorageId(storageId).stream()
                .map(StorageResponseDto::new)
                .collect(Collectors.toList());
    }

    public StorageFile findByStorageIdAndId(Long storageId, Long storageFileId) {
        return storageFileRepository.findByStorageIdAndId(storageId, storageFileId).orElseThrow(() ->
                new CustomException(ErrorCode.STORAGE_FILE_NOT_MATCHED));
    }

    public String saveProfileImage(Long memberId, String previous, MultipartFile file) throws IOException {
        if(previous != null){
            File previousFile = new File(previous);
            boolean fileDeleted = previousFile.delete();
        }

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedPath = basePath + "/profiles/" + memberId + "_profile_image" + extension;

        file.transferTo(new File(savedPath));

        return savedPath;
    }
}
