package com.ssafy.saessak.board.service;


import com.ssafy.saessak.album.domain.Album;
import com.ssafy.saessak.album.domain.File;
import com.ssafy.saessak.album.repository.AlbumRepository;
import com.ssafy.saessak.board.domain.Board;
import com.ssafy.saessak.board.dto.*;
import com.ssafy.saessak.board.repository.BoardRepository;
import com.ssafy.saessak.exception.code.ExceptionCode;
import com.ssafy.saessak.exception.model.NotFoundException;
import com.ssafy.saessak.exception.model.UserException;
import com.ssafy.saessak.oauth.service.AuthenticationService;
import com.ssafy.saessak.user.domain.Classroom;
import com.ssafy.saessak.user.domain.Kid;
import com.ssafy.saessak.user.domain.User;
import com.ssafy.saessak.user.repository.KidRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final KidRepository kidRepository;
    private final AlbumRepository albumRepository;
    private final AuthenticationService authenticationService;

    // crud
    @Transactional
    public Board saveBoard(BoardRequestDto boardRequestDto){
        Optional<Kid> result = kidRepository.findById(boardRequestDto.getKidId());
        if(result.isEmpty()) throw new UserException(ExceptionCode.KID_NOT_FOUND);
        Kid kid = result.get();
        User user = authenticationService.getUserByAuthentication();
        Classroom classroom = user.getClassroom();

        Board saveBoard = Board.builder()
                .kid(kid)
                .classroom(classroom)
                .boardDate(boardRequestDto.getBoardDate())
                .boardContent(boardRequestDto.getBoardContent())
                .boardTemperature(boardRequestDto.getBoardTemperature())
                .boardSleepTime(boardRequestDto.getBoardSleepTime())
                .boardPoopStatus(boardRequestDto.getBoardPoopStatus())
                .boardTall(boardRequestDto.getBoardTall())
                .boardWeight(boardRequestDto.getBoardWeight())
                .build();

        boardRepository.save(saveBoard);

        return saveBoard;
    }

    public BoardDetailDto readBoard(Long boardId) {
        Optional<Board> result = boardRepository.findById(boardId);
        if (result.isEmpty()) throw new NotFoundException(ExceptionCode.BOARD_NOT_FOUND);
        Board board = result.get();

        return BoardDetailDto.builder()
                .boardId(board.getBoardId())
                .kidId(board.getKid().getId())
                .classroomId(board.getClassroom().getClassroomId())
                .boardDate(board.getBoardDate())
                .boardContent(board.getBoardContent())
                .boardTemperature(board.getBoardTemperature())
                .boardSleepTime(board.getBoardSleepTime())
                .boardPoopStatus(board.getBoardPoopStatus())
                .boardTall(board.getBoardTall())
                .boardWeight(board.getBoardWeight())
                .boardPath(board.getBoardPath())
                .build();
    }


    @Transactional
    public Long deleteBoard(Long boardId) {
        Optional<Board> result = boardRepository.findById(boardId);
        if (result.isEmpty()) throw new NotFoundException(ExceptionCode.BOARD_NOT_FOUND);
        Board board = result.get();
        boardRepository.delete(board);

        return board.getBoardId();
    }
    public List<BoardResponseDto> findByKid(Long kidId){
        Optional<Kid> kidResult = kidRepository.findById(kidId);
        if(kidResult.isEmpty()) throw new UserException(ExceptionCode.KID_NOT_FOUND);
        Kid kid = kidResult.get();

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        Optional<List<Board>> boardResult = boardRepository.findByKid(kid);
        if(boardResult.isEmpty()) return boardResponseDtoList;

        for(Board board: boardResult.get()){

            String path = null;
            Optional<List<Album>> albumListResult = albumRepository.findByKidAndAlbumDate(kid,board.getBoardDate());
            if(albumListResult.isPresent()){
                List<Album> albumList = albumListResult.get();
                out : for(Album album : albumList){
                    for(File file : album.getFileList()){
                        path = file.getFilePath();
                        break out;
                    }
                }
            }

            BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                    .boardDate(board.getBoardDate())
                    .boardId(board.getBoardId())
                    .boardPath(path)
                    .kidId(kidId)
                    .build();

            boardResponseDtoList.add(boardResponseDto);
        }
        return boardResponseDtoList;
    }

    public BoardDetailDto findByKidAndDate (Long kidId, LocalDate date){
        Optional<Kid> kidResult = kidRepository.findById(kidId);
        if(kidResult.isEmpty()) throw new UserException(ExceptionCode.KID_NOT_FOUND);
        Kid kid = kidResult.get();

        Optional<List<Board>> result = boardRepository.findByKidAndBoardDate(kid,date);
        if (result.isPresent()){
            Board board = result.get().get(0);
            return BoardDetailDto.builder()
                    .boardId(board.getBoardId())
                    .kidId(board.getKid().getId())
                    .classroomId(board.getClassroom().getClassroomId())
                    .boardDate(board.getBoardDate())
                    .boardPath(board.getBoardPath())
                    .boardContent(board.getBoardContent())
                    .boardPoopStatus(board.getBoardPoopStatus())
                    .boardSleepTime(board.getBoardSleepTime())
                    .boardTall(board.getBoardTall())
                    .boardTemperature(board.getBoardTemperature())
                    .boardWeight(board.getBoardWeight())
                    .build();
        }
        else{
            return null;
        }
    }
    // 아이의 가장 최근 엘범
    public BoardDetailDto getKidCurrentBoard( Long kidId){
        Optional<Kid> kidResult = kidRepository.findById(kidId);
        if(kidResult.isEmpty()) throw new UserException(ExceptionCode.KID_NOT_FOUND);
        Kid kid = kidResult.get();

        Optional<Board> result = boardRepository.findFirstByKidOrderByBoardDateDesc(kid);

        if(result.isEmpty()) return null;

        Board board = result.get();
        return BoardDetailDto.builder()
                .kidId(kidId)
                .classroomId(kid.getClassroom().getClassroomId())
                .boardDate(board.getBoardDate())
                .boardTemperature(board.getBoardTemperature())
                .boardDate(board.getBoardDate())
                .boardWeight(board.getBoardWeight())
                .boardPoopStatus(board.getBoardPoopStatus())
                .boardTall(board.getBoardTall())
                .boardId(board.getBoardId())
                .boardSleepTime(board.getBoardSleepTime())
                .boardContent(board.getBoardContent())
                .build();


    }
    public PhysicalResponseDto getPhysicalList (Long kidId, LocalDate startDate,LocalDate endDate){
        Optional<Kid> kidResult = kidRepository.findById(kidId);
        if(kidResult.isEmpty()) throw new UserException(ExceptionCode.KID_NOT_FOUND);
        Kid kid = kidResult.get();
        List<PhysicalDto> physicalDtoList= new ArrayList<>();

        Optional<List<Board>> physicalResult = boardRepository.findByKidAndBoardDateBetween(kid,startDate, endDate);

        if(physicalResult.isEmpty()) return null;
        List<Board> boardList = physicalResult.get();
        for( Board board : boardList){
            PhysicalDto physicalDto = PhysicalDto.builder()
                    .boardDate(board.getBoardDate())
                    .boardWeight(board.getBoardWeight())
                    .boardTall(board.getBoardTall())
                    .build();
            physicalDtoList.add(physicalDto);
        }

        return PhysicalResponseDto.builder()
                .kidId(kid.getId())
                .gender(kid.getGender())
                .kidBirthday(kid.getKidBirthday())
                .physicalDtoList(physicalDtoList)
                .kidName(kid.getNickname())
                .build();
    }

    public List<ContentResponseDto> getContentList (Long kidId, LocalDate startDate, LocalDate endDate){
        List<ContentResponseDto> contentResponseDtoList = new ArrayList<>();
        Optional<Kid> kidResult = kidRepository.findById(kidId);
        if(kidResult.isEmpty()) throw new UserException(ExceptionCode.KID_NOT_FOUND);
        Kid kid = kidResult.get();
        Optional<List<Board>> boardResult = boardRepository.findByKidAndBoardDateBetween(kid,startDate,endDate);
        if(boardResult.isEmpty()) return null;

        for(Board board : boardResult.get()){
            ContentResponseDto contentResponseDto = ContentResponseDto.builder()
                    .boardDate(board.getBoardDate())
                    .boardContent(board.getBoardContent())
                    .build();

            contentResponseDtoList.add(contentResponseDto);
        }


        return contentResponseDtoList;
    }
}
