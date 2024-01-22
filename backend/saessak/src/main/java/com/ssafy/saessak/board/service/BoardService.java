package com.ssafy.saessak.board.service;


import com.ssafy.saessak.board.domain.Board;
import com.ssafy.saessak.board.dto.BoardDetailDto;
import com.ssafy.saessak.board.dto.BoardRequestDto;
import com.ssafy.saessak.board.dto.BoardResponseDto;
import com.ssafy.saessak.board.repository.BoardRepository;
import com.ssafy.saessak.user.domain.Classroom;
import com.ssafy.saessak.user.domain.Kid;
import com.ssafy.saessak.user.repository.ClassroomRepository;
import com.ssafy.saessak.user.repository.KidRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final KidRepository kidRepository;
    private final ClassroomRepository classroomRepository;

    // crud
    @Transactional
    public Board saveBoard(BoardRequestDto boardRequestDto){
        Kid kid = kidRepository.findById(boardRequestDto.getKidId()).get();
        Classroom classroom = classroomRepository.findById(boardRequestDto.getClassroomId()).get();

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
        Board board = boardRepository.findById(boardId).get();

        return BoardDetailDto.builder()
                .boardId(board.getBoardId())
                .kidId(board.getKid().getKidId())
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
        Board board = boardRepository.findById(boardId).get();
        boardRepository.delete(board);

        return board.getBoardId();
    }
    public List<BoardResponseDto> findByKid(Long kidId){
        Kid kid = Kid.builder().kidId(kidId).build();
        List<Board> result = boardRepository.findByKid(kid).get();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for(Board board: result){
            BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                    .boardDate(board.getBoardDate())
                    .boardId(board.getBoardId())
                    .boardPath(board.getBoardPath())
                    .build();

            boardResponseDtoList.add(boardResponseDto);
        }
        return boardResponseDtoList;
    }

    public BoardDetailDto findByKidAndDate (Long kidId, Date date){
        Kid kid = Kid.builder().kidId(kidId).build();
        Optional<List<Board>> result = boardRepository.findByKidAndBoardDate(kid,date);
        if (result.isPresent()){
            Board board = result.get().get(0);
            return BoardDetailDto.builder()
                    .boardId(board.getBoardId())
                    .kidId(board.getKid().getKidId())
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
}
