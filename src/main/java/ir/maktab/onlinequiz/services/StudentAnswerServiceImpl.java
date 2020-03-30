package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.StudentAnswerDAO;
import ir.maktab.onlinequiz.dto.AddScoreDTO;
import ir.maktab.onlinequiz.dto.ScoresExamDTO;
import ir.maktab.onlinequiz.models.DescriptiveQuestion;
import ir.maktab.onlinequiz.models.MultipleChoiceQuestion;
import ir.maktab.onlinequiz.models.Score;
import ir.maktab.onlinequiz.models.StudentAnswer;
import ir.maktab.onlinequiz.outcome.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class StudentAnswerServiceImpl implements StudentAnswerService {
    final StudentAnswerDAO studentAnswerDAO;

    public StudentAnswerServiceImpl(StudentAnswerDAO studentAnswerDAO) {
        this.studentAnswerDAO = studentAnswerDAO;
    }

    @Override
    public StudentSubmittedExamListOutcome getStudentSubmittedExam(Long examId) {
        Optional<List<StudentAnswer>> optionalStudentAnswerList = studentAnswerDAO.findAllByExam_Id(examId);
        StudentSubmittedExamListOutcome studentSubmittedExamListOutcome = new StudentSubmittedExamListOutcome();
        studentSubmittedExamListOutcome.setStudentSubmittedExamOutcomeList(new HashSet<>());
        if (optionalStudentAnswerList.isPresent()) {
            List<StudentAnswer> studentAnswers = optionalStudentAnswerList.get();
            studentAnswers
                    .forEach(studentAnswer -> {
                        StudentSubmittedExamOutcome studentSubmittedExamOutcome = new StudentSubmittedExamOutcome();
                        studentSubmittedExamOutcome.setStudentId(studentAnswer.getStudent().getId());
                        studentSubmittedExamOutcome.setStudentFirstName(studentAnswer.getStudent().getFirstName());
                        studentSubmittedExamOutcome.setStudentLastName(studentAnswer.getStudent().getLastName());
                        studentSubmittedExamOutcome.setStudentCode(studentAnswer.getStudent().getStudentCode());
                        studentSubmittedExamListOutcome.getStudentSubmittedExamOutcomeList().add(studentSubmittedExamOutcome);
                    });
        }
        return studentSubmittedExamListOutcome;
    }

    @Override
    public StudentAnswerListOutcome getStudentAnswer(Long examId, Long studentId) {
        Optional<List<StudentAnswer>> optionalStudentAnswerList = studentAnswerDAO.findAllByExam_IdAndStudent_Id(examId, studentId);
        StudentAnswerListOutcome studentAnswerListOutcome = new StudentAnswerListOutcome();
        studentAnswerListOutcome.setStudentAnswerOutcomeList(new ArrayList<>());
        if (optionalStudentAnswerList.isPresent()) {
            List<StudentAnswer> studentAnswers = optionalStudentAnswerList.get();
            studentAnswers
                    .forEach(studentAnswer -> {
                        StudentAnswerOutcome studentAnswerOutcome = new StudentAnswerOutcome();
                        studentAnswerOutcome.setQuestionId(studentAnswer.getQuestion().getId());
                        studentAnswerOutcome.setAnswerId(studentAnswer.getId());
                        studentAnswerOutcome.setDefaultAnswer(studentAnswer.getQuestion().getAnswer());
                        studentAnswerOutcome.setStudentAnswer(studentAnswer.getContext());
                        if (studentAnswer.getScore() == null) {
                            AtomicReference<Float> point = new AtomicReference<>(0F);
                            studentAnswer.getQuestion().getScoreList().stream().filter(score -> score.getExam().equals(studentAnswer.getExam())).findAny()
                                    .ifPresentOrElse(score -> point.set(score.getPoint()), () -> point.set(0F));
                            studentAnswerOutcome.setDefaultPoint(point.get());
                        }
                        studentAnswerOutcome.setDefaultPoint(studentAnswer.getScore().getPoint());
                        if (studentAnswer.getQuestion() instanceof MultipleChoiceQuestion)
                            studentAnswerOutcome.setQuestionType("multiple-choice");
                        else
                            studentAnswerOutcome.setQuestionType("descriptive");

                        studentAnswerListOutcome.getStudentAnswerOutcomeList().add(studentAnswerOutcome);
                    });
        }
        return studentAnswerListOutcome;
    }

    @Override
    public Boolean setScoresStudent(Long examId, ScoresExamDTO scoresExamDTO) {
        boolean b = false;
        Optional<List<StudentAnswer>> optionalStudentAnswers = studentAnswerDAO.findAllByExam_IdAndStudent_Id(examId, scoresExamDTO.getStudentId());
        if (optionalStudentAnswers.isPresent()) {
            List<StudentAnswer> studentAnswers = optionalStudentAnswers.get();
            for (AddScoreDTO addScoreDTO : scoresExamDTO.getAddScoresDTO()) {
                Optional<StudentAnswer> optionalStudentAnswer = studentAnswers
                        .stream()
                        .filter(studentAnswer -> studentAnswer.getQuestion().getId().equals(addScoreDTO.getQuestionId()))
                        .findAny();
                if (optionalStudentAnswer.isPresent()) {
                    StudentAnswer studentAnswer = optionalStudentAnswer.get();
                    Optional<Score> optionalScore = studentAnswer.getQuestion().getScoreList()
                            .stream()
                            .filter(score -> score.getExam().getId().equals(examId))
                            .findAny();
                    if (optionalScore.isPresent()) {
                        Score score = optionalScore.get();
                        if (score.getPoint() >= addScoreDTO.getPoint()) {
                            b = true;
                            if (studentAnswer.getScore() == null)
                                studentAnswer.setScore(
                                        new Score(null, addScoreDTO.getPoint(), studentAnswer.getQuestion(), studentAnswer.getExam()));
                            else
                                studentAnswer.getScore().setPoint(addScoreDTO.getPoint());
                            studentAnswerDAO.save(studentAnswer);
                        } else
                            return false;
                    }
                }
            }
        }
        return b;
    }

    @Override
    public StudentScoreOutcome getScore(Long examId, Long studentId) {
        Optional<List<StudentAnswer>> optionalStudentAnswerList = studentAnswerDAO.findAllByExam_IdAndStudent_Id(examId, studentId);
        AtomicReference<Float> multipleChoicesQuestionScore = new AtomicReference<>(0F);
        AtomicReference<Float> descriptiveQuestionScore = new AtomicReference<>(0F);
        if (optionalStudentAnswerList.isPresent()) {
            List<StudentAnswer> studentAnswers = optionalStudentAnswerList.get();
            studentAnswers
                    .stream()
                    .filter(studentAnswer -> studentAnswer.getQuestion() instanceof MultipleChoiceQuestion)
                    .forEach(studentAnswer -> {
                        if (studentAnswer.getScore() != null)
                            multipleChoicesQuestionScore.updateAndGet(v -> v + studentAnswer.getScore().getPoint());
                    });
            studentAnswers
                    .stream()
                    .filter(studentAnswer -> studentAnswer.getQuestion() instanceof DescriptiveQuestion)
                    .forEach(studentAnswer -> {
                        if (studentAnswer.getScore() != null)
                            descriptiveQuestionScore.updateAndGet(v -> v + studentAnswer.getScore().getPoint());
                    });
        }
        return new StudentScoreOutcome(multipleChoicesQuestionScore.get(),
                descriptiveQuestionScore.get(),
                (multipleChoicesQuestionScore.get() + descriptiveQuestionScore.get()));
    }

}
