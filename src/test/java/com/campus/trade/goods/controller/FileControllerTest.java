package com.campus.trade.goods.controller;

import com.campus.trade.common.ApiResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileControllerTest {

    private FileController fileController;

    @BeforeEach
    void setUp() {
        fileController = new FileController();
        ReflectionTestUtils.setField(fileController, "uploadDir", "./target/test-uploads");
    }

    @Test
    void uploadImage_emptyFile_shouldReturn400() {
        MockMultipartFile file = new MockMultipartFile("file", "", "image/png", new byte[0]);

        ApiResult<Map<String, Object>> result = fileController.uploadImage(file);

        assertEquals(400, result.getCode());
        assertEquals("文件为空", result.getMessage());
    }

    @Test
    void uploadImage_nonImageFile_shouldReturn400() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());

        ApiResult<Map<String, Object>> result = fileController.uploadImage(file);

        assertEquals(400, result.getCode());
        assertEquals("仅支持图片格式", result.getMessage());
    }

    @Test
    void uploadImage_nullContentType_shouldReturn400() {
        MockMultipartFile file = new MockMultipartFile("file", "test.bin", null, new byte[]{1, 2, 3});

        ApiResult<Map<String, Object>> result = fileController.uploadImage(file);

        assertEquals(400, result.getCode());
        assertEquals("仅支持图片格式", result.getMessage());
    }

    @Test
    void uploadImage_validPng_shouldReturn200() {
        byte[] content = new byte[100];
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", content);

        ApiResult<Map<String, Object>> result = fileController.uploadImage(file);

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().get("url"));
        assertTrue(result.getData().get("url").toString().startsWith("/uploads/"));
        assertTrue(result.getData().get("url").toString().endsWith(".png"));
    }

    @Test
    void uploadImage_validJpeg_shouldReturn200() {
        byte[] content = new byte[200];
        MockMultipartFile file = new MockMultipartFile("file", "photo.jpeg", "image/jpeg", content);

        ApiResult<Map<String, Object>> result = fileController.uploadImage(file);

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().get("url"));
        assertTrue(result.getData().get("url").toString().endsWith(".jpeg"));
    }

    @Test
    void uploadImage_noExtension_shouldStillUpload() {
        byte[] content = new byte[50];
        MockMultipartFile file = new MockMultipartFile("file", "noext", "image/png", content);

        ApiResult<Map<String, Object>> result = fileController.uploadImage(file);

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertNotNull(result.getData().get("url"));
        assertTrue(result.getData().get("url").toString().startsWith("/uploads/"));
    }

    @Test
    void uploadImage_urlShouldBeUnique() {
        byte[] content = new byte[100];
        MockMultipartFile file1 = new MockMultipartFile("file", "a.png", "image/png", content);
        MockMultipartFile file2 = new MockMultipartFile("file", "b.png", "image/png", content);

        ApiResult<Map<String, Object>> r1 = fileController.uploadImage(file1);
        ApiResult<Map<String, Object>> r2 = fileController.uploadImage(file2);

        assertNotEquals(r1.getData().get("url"), r2.getData().get("url"));
    }
}