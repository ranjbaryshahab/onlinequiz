package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.ScoresExamDTO;
import ir.maktab.onlinequiz.outcome.StudentAnswerListOutcome;
import ir.maktab.onlinequiz.outcome.StudentScoreOutcome;
import ir.maktab.onlinequiz.outcome.StudentSubmittedExamListOutcome;
import ir.maktab.onlinequiz.services.StudentAnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class StudentAnswerController {
    final StudentAnswerService studentAnswerService;

    public StudentAnswerController(StudentAnswerService studentAnswerService) {
        this.studentAnswerService = studentAnswerService;
    }

    @PostMapping("/teacher/teacher-course/exam/student/answer/{examId}")
    private StudentSubmittedExamListOutcome getStudentSubmittedExam(@PathVariable Long examId) {
        return studentAnswerService.getStudentSubmittedExam(examId);
    }

    @PostMapping("/teacher/teacher-course/exam/{examId}/student/{studentId}/answer")
    private StudentAnswerListOutcome getStudentAnswer(@PathVariable Long examId, @PathVariable Long studentId) {
        return studentAnswerService.getStudentAnswer(examId, studentId);
    }

    @PostMapping("/teacher/teacher-course/exam/student/score/{examId}")
    private ResponseEntity getStudentAnswer(@PathVariable Long examId, @RequestBody ScoresExamDTO scoresExamDTO) {
        if (!studentAnswerService.setScoresStudent(examId, scoresExamDTO))
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("نمرات وارد شده بیشتر از سقف معین می باشد!");
        return ResponseEntity.ok("نمرات با موفقیت ثبت شد");
    }

    @GetMapping("/student/{studentId}/student-course/exam/{examId}/score")
    private StudentScoreOutcome getScoreForStudent(@PathVariable Long examId, @PathVariable Long studentId) {
        return studentAnswerService.getScore(examId, studentId);
    }

    @GetMapping("/teacher/student-course/exam/{examId}/{studentId}/score")
    private StudentScoreOutcome getScoreForTeacher(@PathVariable Long examId, @PathVariable Long studentId) {
        return studentAnswerService.getScore(examId, studentId);
    }
}
