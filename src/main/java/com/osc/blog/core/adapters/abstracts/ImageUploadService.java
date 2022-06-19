package com.osc.blog.core.adapters.abstracts;

import com.osc.blog.core.utilities.results.DataResult;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    DataResult<String> uploadImage(MultipartFile image, String folder);
    DataResult<String> uploadUserPhoto(MultipartFile image);
    DataResult<String> uploadArticlePhoto(MultipartFile image);

}
