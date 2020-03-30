package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.AnswersExamDTO;
import ir.maktab.onlinequiz.dto.CreateExamDTO;
import ir.maktab.onlinequiz.dto.IdsListDTO;
import ir.maktab.onlinequiz.dto.QuestionDTO;
import ir.maktab.onlinequiz.services.ExamService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/teacher/teacher-course/edit-exam-from-course")
    private void editExam(@RequestBody CreateExamDTO createExamDTO) {
        examService.editExam(createExamDTO);
    }

    @PostMapping("/teacher/teacher-course/start-exam/{id}")
    private void startExam(@PathVariable Long id) {
        examService.startExam(id);
    }

    @PostMapping("/teacher/teacher-course/end-exam/{id}")
    private void endExam(@PathVariable Long id) {
        examService.endExam(id);
    }

    @GetMapping("/student/student-course/exam/get-time/{id}")
    private Long getExamTime(@PathVariable Long id) {
        return examService.getExamTime(id);
    }

    @PostMapping("/student/student-course/exam/answer/{examId}")
    private void addAnswerByStudent(@PathVariable Long examId, @RequestBody AnswersExamDTO answersExamDTO) {
        examService.addAnswerByStudent(examId, answersExamDTO);
    }
}
