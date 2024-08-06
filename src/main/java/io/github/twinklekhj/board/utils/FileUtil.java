package io.github.twinklekhj.board.utils;

import io.github.twinklekhj.board.exception.FileCannotUploadException;
import io.github.twinklekhj.board.api.vo.FileVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
public class FileUtil {
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "png", "jpeg"};
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/jpg"
    );

    public static List<FileVO> getFileList(String path) {
        List<FileVO> resultList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        File dir = new File(path);
        if (!dir.exists()) {
            // 경로가 없을 경우 생성
            dir.mkdirs();
            log.error("해당 경로가 없어 생성하였습니다. path: {}", path);
            return Collections.emptyList();
        }

        // 파일명 필터
        File[] files = dir.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }

        BasicFileAttributes attrs;
        for (File file : files) {
            // 폴더인 경우 pass
            if (file.isDirectory())
                continue;

            try {
                attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            } catch (IOException e) {
                log.error("I/O 작업중 에러가 발생하였습니다. msg: {}", e.getMessage());
                return Collections.emptyList();
            }
            FileTime createDate = attrs.creationTime();
            FileTime editDate = attrs.lastModifiedTime();
            FileTime accessDate = attrs.lastAccessTime();

            resultList.add(FileVO.builder()
                    .name(file.getName())
                    .path(file.getPath())
                    .size(file.length())
                    .createDate(convertLocalDateTimeBy(createDate))
                    .editDate(convertLocalDateTimeBy(editDate))
                    .accessDate(convertLocalDateTimeBy(accessDate))
                    .build());
        }

        return resultList;
    }

    public static LocalDateTime convertLocalDateTimeBy(FileTime time) {
        return LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault());
    }

    /**
     * [FileUtil] 파일 업로드
     *
     * @param path       파일 경로
     * @param uploadFile 업로드할 파일
     * @param uploadName 파일명 (확장자 미포함)
     * @return 업로드 파일명 (확장자 포함)
     */
    public static Optional<String> uploadFile(String path, MultipartFile uploadFile, String uploadName) {
        String result;

        // files 폴더는 직접 생성해주지 않으면 Not Found Error.
        File folder = new File(path);
        if (!folder.exists()) {
            if(folder.mkdirs()){
                log.info("폴더가 생성되었습니다. folder path: {}", folder);
            }
        }

        String fileName = uploadFile.getOriginalFilename();
        String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1);

        // 웹 쉘 확장자를 가진 경우
        String[] candidates = {"jpg", "png", "jpeg"};
        if (Arrays.stream(candidates).noneMatch(ext -> ext.equalsIgnoreCase(fileExt))) {
            log.info("denied file ext: {}", fileExt);
            throw new FileCannotUploadException("업로드할 수 없는 파일 확장자입니다");
        }

        // Check if the content type is allowed
        String contentType = uploadFile.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            log.info("Denied content type: {}", contentType);
            throw new FileCannotUploadException("업로드할 수 없는 파일 유형입니다");
        }

        byte[] bytes;
        try {
            bytes = uploadFile.getBytes();
        } catch (IOException e) {
            log.error("파일 Byte 변환시 Error 발생: {}", e.getMessage());
            throw new FileCannotUploadException("잘못된 파일입니다.");
        }

        if (uploadName.isEmpty()) {
            return Optional.empty();
        }

        // PNG 파일로만 저장
        result = String.format("%s.png", uploadName);
        String filePath = String.format("%s/%s", folder.getAbsolutePath(), result);

        // 파일 생성
        File file = new File(filePath);
        // 파일이 존재할 경우 덮어쓰기
        if (file.exists()) {
            // 실행 가능한 파일이면 실행권한 제거
            if (file.canExecute()) {
                file.setExecutable(false);
            }
        }
        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            out.write(bytes);
        } catch (Exception e) {
            throw new FileCannotUploadException(e.getMessage());
        }

        return Optional.of(result);
    }

    /**
     * [FileUtil] 폴더 삭제
     *
     * @param path 파일 경로
     * @return delete 여부
     */
    public static int deleteFolder(String path) {
        int count = 0;

        File directory = new File(path);
        if (directory.isDirectory()) {
            count = Objects.requireNonNull(directory.list()).length;
            try {
                FileUtils.cleanDirectory(directory);
            } catch (IOException e) {
                log.error("ERROR!! MSG: 폴더 삭제중 오류가 발생하였습니다. - {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }

        return count;
    }

    /**
     * [FileUtil] 파일 삭제
     *
     * @param path     파일 경로
     * @param fileName 파일명
     * @return delete 여부
     */
    public static boolean deleteFile(String path, String fileName) {
        File file = new File(String.format("%s\\%s", path, fileName));
        if (file.exists()) {
            if (file.delete()) {
                return true;
            }
        }

        return false;
    }

    public static boolean downloadFile(HttpServletRequest request, HttpServletResponse response, String path,
                                       String fileName) {
        File file = new File(String.format("%s\\%s", path, fileName));
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            log.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(), file.canRead());
            return false;
        }

        response.setContentType("application/octet-stream; charset=utf-8");
        response.setContentLength((int) file.length());

        try (OutputStream out = response.getOutputStream(); FileInputStream fis = new FileInputStream(file)) {
            String browser = getBrowser(request);
            String disposition = getDisposition(fileName, browser);
            response.setHeader("Content-Disposition", disposition);
            response.setHeader("Content-Transfer-Encoding", "binary");

            FileCopyUtils.copy(fis, out);
        } catch (IOException e) {
            log.error("File 다운로드 중 에러가 발생하였습니다. Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return true;
    }

    public static byte[] downloadFile(String path,
                                      String fileName) {
        File file = new File(String.format("%s\\%s", path, fileName));
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            log.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(), file.canRead());
            return null;
        }

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(), file.canRead());
            return null;
        }
    }

    private static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.contains("MSIE") || header.contains("Trident"))
            return "MSIE";
        else if (header.contains("Chrome"))
            return "Chrome";
        else if (header.contains("Opera"))
            return "Opera";
        return "Firefox";
    }

    private static String getDisposition(String filename, String browser) throws UnsupportedEncodingException {
        String dispositionPrefix = "attachment;filename=";
        String encodedFilename = null;
        switch (browser) {
            case "MSIE":
                encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
                break;
            case "Firefox":
                encodedFilename = String.format("\"%s\"", new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
                break;
            case "Opera":
                encodedFilename = String.format("\"%s\"", new String(filename.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                break;
            case "Chrome":
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < filename.length(); i++) {
                    char c = filename.charAt(i);
                    if (c > '~') {
                        sb.append(URLEncoder.encode(String.valueOf(c), "UTF-8"));
                    } else {
                        sb.append(c);
                    }
                }
                encodedFilename = sb.toString();
                break;
        }
        return dispositionPrefix + encodedFilename;
    }

    public static String getRealPath(HttpServletRequest request, String path) {
        return request.getSession().getServletContext().getRealPath(path);
    }
}