package com.example.prj1be.service;

import com.example.prj1be.domain.Board;
import com.example.prj1be.domain.BoardFile;
import com.example.prj1be.domain.Member;
import com.example.prj1be.mapper.BoardMapper;
import com.example.prj1be.mapper.CommentMapper;
import com.example.prj1be.mapper.FileMapper;
import com.example.prj1be.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class) // 두개를 같이 실행시킬떄 하나만 성공할경우 성공한것을 롤백시킴
public class BoardService {

    private final BoardMapper mapper;
    private final CommentMapper commentMapper;
    private final LikeMapper likeMapper;
    private final FileMapper fileMapper;

    private final S3Client s3;

    @Value("${image.file.prefix}")
    private String urlPrefix;
    @Value("${aws.s3.bucket.name}")
    private String bucket;

    public boolean save(Board board, MultipartFile[] files, Member login) throws IOException {
        //
        board.setWriter(login.getId());

        int cnt = mapper.insert(board);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // boardId, name
                fileMapper.insert(board.getId(), files[i].getOriginalFilename());

                // 실제 파일을 S3 bucket(aws)에 upload
                // 일단 local에 저장
                upload(board.getId(), files[i]);
            }
        }


        return cnt == 1;
    }

    private void upload(Integer boardId, MultipartFile file) throws IOException {
        // 로컬 저장 코드
//        // 파일 저장 경로
//        // C:\Temp\prj1\게시물번호\파일명
//        File folder = new File("C:\\Temp\\prj1\\" + boardId);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//
//        String path = folder.getAbsolutePath() + "\\" + file.getOriginalFilename();
//        File des = new File(path);
//        // input, output strema 을 자동으로 처리해주는 메소드(file.transferTo(new File(path));
//        file.transferTo(new File(path));

        // aws 버킷s3 에 저장하는 코드
        String key = "prj1/" + boardId + "/" + file.getOriginalFilename();
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

    }

    public boolean validate(Board board) {
        if (board == null) {
            return false;
        }

        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }

        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }

        return true;
    }

    // 마찬가지로 매개변수에 String keyword 추가
    public Map<String, Object> list(Integer page, String keyword) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

//        int countAll = mapper.countAll();
        int countAll = mapper.countAll("%" + keyword + "%");
        int lastPageNumber = (countAll - 1) / 10 + 1;
        int startPageNumber = (page - 1) / 10 * 10 + 1;
        int endPageNumber = startPageNumber + 9;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);
        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        int from = (page - 1) * 10;
        // "%"+keyword+"%" 를 추가하게 해주면 mapper에서 <script> 사용할 필요없음.
        map.put("boardList", mapper.selectAll(from, "%" + keyword + "%"));
        map.put("pageInfo", pageInfo);
        return map;
    }

    public Board get(Integer id) {
        Board board = mapper.selectById(id);

        List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);

        for (BoardFile boardFile : boardFiles) {
            String url = urlPrefix + "prj1/" + id + "/" + boardFile.getName();
            boardFile.setUrl(url);
        }

        board.setFiles(boardFiles);

        return board;
    }

    public boolean remove(Integer id) {
        // 게시물에 달린 댓글들 지우기
        commentMapper.deleteByBoardId(id);

        // 좋아요 레코드 지우기
        likeMapper.deleteByBoardId(id);

        // aws s3에 있는 파일 삭제
        deleteFile(id);

        return mapper.deleteById(id) == 1;
    }

    // aws s3에 있는 파일 삭제
    private void deleteFile(Integer id) {
        // 파일명 조회
        List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);

        // s3 bucket objects 지우기
        for (BoardFile file : boardFiles) {
            String key = "prj1/" + id + "/" + file.getName();

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3.deleteObject(deleteObjectRequest);
        }

        // 첨부파일 레코드 지우기
        fileMapper.deleteByBoardId(id);

    }

    public boolean update(Board board, MultipartFile[] files) {
        // 업데이트 하기전에 기존 첨부파일 레코드 지우기
        fileMapper.deleteByBoardId(board.getId());

        // 로컬 저장 코드
        // 파일 저장 경로
        // C:\Temp\prj1\게시물번호\파일명
        File folder = new File("C:\\Temp\\prj1\\" + board.getId());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (int i = 0; i < files.length; i++) {
            String path = folder.getAbsolutePath() + "\\" + files[i].getOriginalFilename();
            File des = new File(path);

            try {
                files[i].transferTo(des);
            } catch (IOException e) {
                e.printStackTrace(); // 또는 로깅을 활용하여 예외를 기록
                return false; // 파일 전송 실패 시 메서드 종료
            }
        }

        // 저장버튼 눌렀을 때의 업데이트 되는 내용
        return mapper.update(board) == 1;
    }

    public boolean hasAccess(Integer id, Member login) {
        if (login == null) {
            return false;
        }

        if (login.isAdmin()) {
            return true;
        }

        Board board = mapper.selectById(id);

        if (board == null || board.getWriter() == null) {
            return false;
        }

        return board.getWriter().equals(login.getId());
    }

}
