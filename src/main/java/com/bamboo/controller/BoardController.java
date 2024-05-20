package com.bamboo.controller;

import com.bamboo.dto.BoardDto;
import com.bamboo.service.BoardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = "/boards/{boardId}")
    public String boardDtl(@PathVariable("boardId") Long boardId, Model model){
        try{
            BoardDto boardDto = new BoardDto();
            model.addAttribute("boardDto", boardDto);
        }catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 게시글 입니다.");
            model.addAttribute("boardDto", new BoardDto());
            return "board/boardDtl";
        }
        return "board/boardDtl";
    }

    @GetMapping(value = "/boards/create")
    public String newBoard(Model model){
        model.addAttribute("boardDto", new BoardDto());
        return "board/boardForm";
    }

    @PostMapping(value = "/boards/create")
    public String boardNew(BoardDto boardDto, BindingResult bindingResult,
                           Model model, @RequestParam("boardFiles") List<MultipartFile> boardFileList){
        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }
        try {
            boardService.saveBoard("test@test.com",boardDto, boardFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }

        return "redirect:/";
    }
}
