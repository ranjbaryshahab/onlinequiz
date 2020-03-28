package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.QuestionDTO;
import ir.maktab.onlinequiz.models.DescriptiveQuestion;
import ir.maktab.onlinequiz.models.MultipleChoiceQuestion;
import ir.maktab.onlinequiz.services.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class QuestionController {
    final QuestionService questionService;


    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/teacher/teacher-course/exam/question-list/multiple-choice/{examId}/{pageNo}/{pageSize}")
    private Page<MultipleChoiceQuestion> multipleQuestionPaginate(@PathVariable Long examId, @PathVariable int pageNo, @PathVariable int pageSize) {
        return questionService.multipleChoiceQuestionPaginate(examId, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/teacher/teacher-course/exam/question-list/descriptive/{examId}/{pageNo}/{pageSize}")
    private Page<DescriptiveQuestion> descriptiveQuestionPaginate(@PathVariable Long examId, @PathVariable int pageNo, @PathVariable int pageSize) {
        return questionService.descriptiveQuestionPaginate(examId, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/teacher/question-bank/list/multiple-choice/{username}/{pageNo}/{pageSize}")
    private Page<MultipleChoiceQuestion> multipleQuestionPaginateTeacherQuestionBank(@PathVariable String username, @PathVariable int pageNo, @PathVariable int pageSize) {
        return questionService.multipleQuestionPaginateTeacherQuestionBank(username, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/teacher/question-bank/list/descriptive/{username}/{pageNo}/{pageSize}")
    private Page<DescriptiveQuestion> descriptiveQuestionPaginateTeacherQuestionBank(@PathVariable String username, @PathVariable int pageNo, @PathVariable int pageSize) {
        return questionService.descriptiveQuestionPaginateTeacherQuestionBank(username, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/teacher/teacher-course/edit-question")
    private void editQuestion(@RequestBody QuestionDTO questionDTO) {
        questionService.editQuestion(questionDTO);
    }

    @PostMapping("/student/student-course/exam/question-list/multiple-choice/{examId}")
    private List<MultipleChoiceQuestion> multipleQuestionPaginateStudent(@PathVariable Long examId) {
        return questionService.multipleChoiceQuestionPaginateStudent(examId);
    }

    @PostMapping("/student/student-course/exam/question-list/descriptive/{examId}")
    private List<DescriptiveQuestion> descriptiveQuestionPaginateStudent(@PathVariable Long examId) {
        return questionService.descriptiveQuestionPaginateStudent(examId);
    }
}
