package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.IdsListDTO;
import ir.maktab.onlinequiz.dto.QuestionDTO;
import ir.maktab.onlinequiz.services.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class ExamController {

    final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/teacher/teacher-course/add-question-to-exam")
    private void createQuestion(@RequestBody QuestionDTO questionDTO) {
        examService.createQuestion(questionDTO);
    }

    @PostMapping("/teacher/question-bank/add-question-to-exam")
    private void addQuestionFromTeacherQuestionBankToExam(@RequestBody IdsListDTO idsListDTO) {
        examService.addQuestionFromTeacherQuestionBankToExam(idsListDTO);
    }

    @PostMapping("/teacher/teacher-course/exam/questions/delete-all-selected")
    private void deleteAllSelectedQuestionOfExam(@RequestBody IdsListDTO idsListDTO) {
        examService.deleteAllSelectedQuestionOfExam(idsListDTO);
    }

    @PostMapping("/teacher/teacher-course/exam/{examId}/questions/delete-all")
    private void deleteAllQuestions(@PathVariable Long examId) {
        examService.deleteAllQuestionOfExam(examId);
    }
}
